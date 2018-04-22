package pennychain.center;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import pennychain.controller.Map;
import pennychain.controller.projResource;

public class OptimizationRequest {
    private int max_posi; //max # of resources to exist on returned map
    private Map map;
    private ArrayList<projResource> projectResourceList;


    public OptimizationRequest(ArrayList<projResource> projectResourceList) {
        this.projectResourceList = projectResourceList;
    }

    /* sendRequest:
     *  Called to initiate an optimization request on the current project's map
     *  Params: path_name, a string containing the path to exchange files with APMpython optimizer
     *          projectMap, a Map containing the longitude/latitude of each existing and considerable resource position
     *
     */
    public String[] sendRequest(String path_name, Map projectMap) throws IOException {

        this.map = projectMap;
        double[] latitudes = map.getGridLats();
        double[] longitudes = map.getGridLongs();
        String origins = "";
        String destinations = "";
        int resAmt_origins = 0;
        int resAmt_destinations = 0;
        for(int i = 0; i < projectResourceList.size(); i++) {
            int[][] placement = projectResourceList.get(i).getPlacement();
            for(int j = 0; j < placement.length; j++) {
                for(int t = 0; t < placement[j].length; t++) {
                    if(placement[t][j] == 1) {
                        if(projectResourceList.get(i).getrType() == 0){
                            if(destinations.equals("")) {
                                destinations += Double.toString(latitudes[placement[j].length*j+t]) + "," + Double.toString(longitudes[placement[j].length*j+t]);
                                resAmt_destinations++;
                            } else {
                                destinations += "|" + Double.toString(latitudes[placement[j].length*j+t]) + "," + Double.toString(longitudes[placement[j].length*j+t]);
                                resAmt_destinations++;
                            }
                        } else if(projectResourceList.get(i).getrType() == 1){
                            if(origins.equals("")) {
                                origins += Double.toString(latitudes[placement[0].length * j + t]) + "," + Double.toString(longitudes[placement[0].length * j + t]);
                                resAmt_origins++;
                            } else {
                                origins += "|"+Double.toString(latitudes[placement[0].length * j + t]) + "," + Double.toString(longitudes[placement[0].length * j + t]);
                                resAmt_origins++;

                            }
                        }
                    }
                }
            }
        }

        System.out.println("O: " + origins);
        System.out.println("D: " + destinations);
        String[] distanceMatrix = this.getMinDistanceSupply(googleDistanceMatrix(origins, destinations), resAmt_origins, resAmt_destinations);

        this.createOptimizableFile(distanceMatrix);

        APMpyth apmpython = new APMpyth(path_name);
        /*
         * Currently python is returning: "Model file file_7158.apm does not exist"
         * (the 4-digit number following file_ varies every run)
         *
         * Not 100% sure what cause is. TODO: check with Hayden (perhaps optimization.py is referencing unknown file?)
         *
         */


        System.out.println(apmpython.sendData());
        return null;
    }

    private void display_on_map(String map) {
        /*
         * Convert file from APMPyth to Map type to display on GUI
         *
         *
         */
    }


    /*createOptimizableFile  (may split into multiple methods later)
     * Extracts data and writes file readable by APMPython
     *
     * TODO: add census weighting for demand to objective funtion
     */
    private void createOptimizableFile(String[] distance) throws IOException {
        FileWriter f = new FileWriter("pennychain\\center\\file.apm");
        FileWriter f0 = new FileWriter("pennychain\\center\\variables.txt");
        ArrayList<String> sales_centers = new ArrayList<>();
        ArrayList<String> dist_centers = new ArrayList<>();
        ArrayList<String> constraints = new ArrayList<>();

        for (int i = 0; i < projectResourceList.size(); i++) {
            if (projectResourceList.get(i).getrType() == 0) {
                sales_centers.add("X["+i+"]");
                constraints.add("X["+i+"]=0");
            } else if (projectResourceList.get(i).getrType() == 1) {
                dist_centers.add("X["+i+"]");
                //add constraints pertaining to this variable
            } else {
                sales_centers.add("X["+i+"]");
                constraints.add("X["+i+"]=0");
            }
        }

        String max_num_centers = "";
        for (int i = 0; i < sales_centers.size(); i++) {
            f0.write("int_" + sales_centers.get(i) + System.getProperty("line.separator"));
            max_num_centers = max_num_centers + "int_" + sales_centers.get(i) + "+";
        }
        f0.close();
        constraints.add(max_num_centers + "0 =" + max_posi);

        f.write("Model" + System.getProperty("line.separator") + "  Variables" + System.getProperty("line.separator"));

        String obj_func = "Minimize ";
        for (int i = 0; i < sales_centers.size(); i++) {
            obj_func = obj_func + (distance[i] /*( * gasPrice[i] )*/ + " * int_" + sales_centers.get(i) + " + ");
        }   //TODO: calculate demand from census data
        obj_func = obj_func + "0 " + System.getProperty("line.separator") + "  End Equations" + System.getProperty("line.separator") + "End Model";

        while (!(sales_centers.isEmpty())) {
            f.write("    int_" + sales_centers.get(0) + " = 0, >=0, <=1" + System.getProperty("line.separator"));
            sales_centers.remove(0);
        }
        f.write("  End Variables" + System.getProperty("line.separator") + System.getProperty("line.separator") + "  Equations" + System.getProperty("line.separator"));
        while (!(constraints.isEmpty())) {
            f.write("    int_" + constraints.get(0) + System.getProperty("line.separator"));
            constraints.remove(0);
        }

        f.write(System.getProperty("line.separator") + "    " + obj_func);
        f.close();
    }

    /*setMaxCenters:
     * Sets maximum amount of resources, including pre-existing ones, to include in optimized result
     * Params: user specified maximum amount of resources
     */
    public void setMaxCenters(int desired_max) {
        this.max_posi = desired_max; //needs update for new constraint system
    }


    /*
     *  Below is Google Maps API HTTP Requests
     *  and parsing methods for JSON results
     *
     */
    private String googleReverseGeocode(double lat, double lng) {
        try {
            URL myURL = new URL("https://maps.googleapis.com/maps/api/geocode/json?latlng=" + lat + "," + lng + "&key=AIzaSyD7MjGyPPBoWoQaqQinDGn3lnn5P_9sL_w");
            HttpURLConnection myURLConnection = (HttpURLConnection) myURL.openConnection();
            myURLConnection.connect();
            int status = myURLConnection.getResponseCode();

            switch (status) {
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(myURLConnection.getInputStream()));
                    String tempStr = "";
                    StringBuilder sb = new StringBuilder();
                    while (null != (tempStr = br.readLine())) {
                        if(tempStr.contains("formatted_address")) {
                            sb.append(tempStr + '\n');
                            System.out.println(tempStr);
                            break;
                        }
                    }
                    return sb.toString();
            }
        } catch (MalformedURLException e) {
            // new URL() failed
            // ...
        } catch (IOException e) {
            // openConnection() failed
            // ...
        }
        return null;
    }

    //get distances from supply centers to potential sales centers
    private String googleDistanceMatrix(String origins, String destinations) {
        try {
            URL myURL = new URL("https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=" + origins + "&destinations=" + destinations + "&key=AIzaSyD7MjGyPPBoWoQaqQinDGn3lnn5P_9sL_w");
            HttpURLConnection myURLConnection = (HttpURLConnection) myURL.openConnection();
            myURLConnection.connect();
            int status = myURLConnection.getResponseCode();

            switch (status) {
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(myURLConnection.getInputStream()));
                    String tempStr = "";
                    StringBuilder sb = new StringBuilder();
                    while (null != (tempStr = br.readLine())) {
                        if(tempStr.contains("distance")) {
                            String nxt = br.readLine();
                            nxt = nxt.substring(28, nxt.length()-5);
                            sb.append(nxt + '\n');
                        }
                    }
                    return sb.toString();
            }
        } catch (MalformedURLException e) {
            // new URL() failed
            // ...
        } catch (IOException e) {
            // openConnection() failed
            // ...
        }
        return null;
    }

    private String googleDirections(String origin, String destination) {
        try {
            URL myURL = new URL("https://maps.googleapis.com/maps/api/directions/json?origin=" + origin + "&destination=" + destination + "&key=AIzaSyD7MjGyPPBoWoQaqQinDGn3lnn5P_9sL_w");
            HttpURLConnection myURLConnection = (HttpURLConnection) myURL.openConnection();
            myURLConnection.connect();
            int status = myURLConnection.getResponseCode();

            switch (status) {
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(myURLConnection.getInputStream()));
                    String tempStr = "";
                    StringBuilder sb = new StringBuilder();
                    while (null != (tempStr = br.readLine())) {
                        sb.append(tempStr + '\n');
                        System.out.println(tempStr);
                    }
                    return sb.toString();
            }
        } catch (MalformedURLException e) {
            // new URL() failed
            // ...
        } catch (IOException e) {
            // openConnection() failed
            // ...
        }
        return null;
    }


    private String[] getMinDistanceSupply(String distances, int origins, int destinations) throws IOException {
        String[] minDistance = new String[destinations];
        BufferedReader br = new BufferedReader(new StringReader(distances));
        String currentDistance;
        for(int i = 0; i < origins; i++) {
            for(int j = 0; j < destinations; j++) {
                currentDistance = br.readLine();
                if(minDistance[j] == null) {
                    minDistance[j] = currentDistance;
                } else if(Double.parseDouble(minDistance[j]) < Double.parseDouble(currentDistance)) {
                    minDistance[j] = currentDistance;
                }
            }
        }
        return minDistance;
    }
}