package pennychain.center;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import pennychain.controller.Map;
import pennychain.controller.projResource;

public class OptimizationRequest {
    //private int max_posi; //max # of resources to exist on returned map
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

        ArrayList<String> sales_centers = new ArrayList<>();
        //ArrayList<String> dist_centers = new ArrayList<>();
        ArrayList<String> constraints = new ArrayList<>();

        if(projectResourceList.get(0) != null) {

            //Create a matrix combining the placement of all existing project resources
            int[][] init_size = projectResourceList.get(0).getPlacement();
            int[][] globalResourceMatrix = new int[init_size[0].length][init_size.length];  //0: potential destination, 1: origin, >1: existing destination, <-1: blocked location
            double[] latitudes = map.getGridLats();
            double[] longitudes = map.getGridLongs();

            for (int i = 0; i < projectResourceList.size(); i++) {
                int[][] placement = projectResourceList.get(i).getPlacement();

                if (projectResourceList.get(i).getrType() == 1) { //origins
                    for (int y = 0; y < placement.length; y++) {
                        for (int x = 0; x < placement[y].length; x++) {
                            if (placement[x][y] == 1) {
                                globalResourceMatrix[x][y] = 1;
                            }
                        }
                    }
                } else {
                    for (int y = 0; y < placement.length; y++) {
                        for (int x = 0; x < placement[y].length; x++) {
                            if (placement[x][y] == 1) {
                                globalResourceMatrix[x][y] = (i+2);
                            } else if (placement[x][y] == -1) {
                                globalResourceMatrix[x][y] = ((i+2)*-1);
                            }
                        }
                    }
                }
            }

            //Create variables and constraints
            for(int i = 0; i < projectResourceList.size(); i++) {
                if (projectResourceList.get(i).getrType() != 1) {
                    String origins = "";
                    int resAmt_origins = 0;
                    String destinations = "";
                    int resAmt_destinations = 0;
                    sales_centers.clear();
                    constraints.clear();
                    int index = 0;

                    for (int y = 0; y < globalResourceMatrix.length; y++) {
                        for (int x = 0; x < globalResourceMatrix[y].length; x++) {

                            if (globalResourceMatrix[x][y] == 0) { //potential (empty) location
                                if (destinations.equals("")) {
                                    sales_centers.add("int_x[" + index + "]");
                                    destinations += Double.toString(latitudes[globalResourceMatrix[0].length * y + x]) + "," + Double.toString(longitudes[globalResourceMatrix[0].length * y + x]);
                                    resAmt_destinations++;
                                    index++;
                                } else {
                                    sales_centers.add("int_x[" + index + "]");
                                    destinations += "|" + Double.toString(latitudes[globalResourceMatrix[0].length * y + x]) + "," + Double.toString(longitudes[globalResourceMatrix[0].length * y + x]);
                                    resAmt_destinations++;
                                    index++;
                                }
                            } else if (globalResourceMatrix[x][y] == 1) { //existing origin
                                if (origins.equals("")) {
                                    origins += Double.toString(latitudes[globalResourceMatrix[0].length * y + x]) + "," + Double.toString(longitudes[globalResourceMatrix[0].length * y + x]);
                                    resAmt_origins++;
                                    //dist_centers.add("int_x[" + (x+y) + "]");
                                    //constraints.add("int_x[" + index + "]=1");
                                    index++;
                                } else {
                                    origins += "|" + Double.toString(latitudes[globalResourceMatrix[0].length * y + x]) + "," + Double.toString(longitudes[globalResourceMatrix[0].length * y + x]);
                                    resAmt_origins++;
                                    //dist_centers.add("int_x[" + (x+y) + "]");
                                    //constraints.add("int_x[" + index + "]=1");
                                    index++;
                                }
                            } else if (globalResourceMatrix[x][y] == (i + 2)) { //existing destination
                                if (destinations.equals("")) {
                                    destinations += Double.toString(latitudes[globalResourceMatrix[0].length * y + x]) + "," + Double.toString(longitudes[globalResourceMatrix[0].length * y + x]);
                                    resAmt_destinations++;
                                    sales_centers.add("int_x[" + index + "]");
                                    constraints.add("int_x[" + index + "]=1");
                                    index++;
                                } else {
                                    destinations += "|" + Double.toString(latitudes[globalResourceMatrix[0].length * y + x]) + "," + Double.toString(longitudes[globalResourceMatrix[0].length * y + x]);
                                    resAmt_destinations++;
                                    sales_centers.add("int_x[" + index + "]");
                                    constraints.add("int_x[" + index + "]=1");
                                    index++;
                                }
                            } else if (globalResourceMatrix[x][y] == ((i + 2) * -1)) { //location blocked
                                //sales_centers.add("int_x["+(x+(x*y))+"]");
                                //constraints.add("int_x["+(x+(x*y))+"]=0");
                            }
                        }
                    }

                    System.out.println("O: " + origins);
                    System.out.println("D: " + destinations);
                    String[] distanceMatrix = this.getMinDistanceSupply(googleDistanceMatrix(origins, destinations), resAmt_origins, resAmt_destinations);

                    this.createOptimizableFile(distanceMatrix, projectResourceList.get(i).getDesired_amnt(), sales_centers, constraints);

                    APMpyth apmpython = new APMpyth(path_name);
                    System.out.println(apmpython.sendData());
                }
            }
        }
        return null;
    }

    private void display_on_map(String map) {
        /*
         * Convert file from APMPyth to Map type to display on GUI
         *
         *
         */
    }


    /*createOptimizableFile
     * Extracts data and writes file readable by APMPython
     */
    private void createOptimizableFile(String[] distance, int max_posi, ArrayList<String> sales_centers, ArrayList<String> constraints) throws IOException {
        FileWriter apmWriter = new FileWriter("pennychain\\center\\file.apm");
        FileWriter varWriter = new FileWriter("pennychain\\center\\variables.txt");

        //Write variable file + constraint for max amount of resource to include in optimized result
        String max_num_centers = "";
        for (int i = 0; i < sales_centers.size(); i++) {
            varWriter.write(sales_centers.get(i) + System.getProperty("line.separator"));
            max_num_centers = max_num_centers + sales_centers.get(i) + "+";
        }
        varWriter.close();
        constraints.add(max_num_centers + "0 =" + max_posi);

        apmWriter.write("Model" + System.getProperty("line.separator") + "  Variables" + System.getProperty("line.separator"));

        //create objective function
        String obj_func = "Minimize ";
        for (int i = 0; i < sales_centers.size(); i++) {
            obj_func = obj_func + (distance[i] /*( * gasPrice[i] )*/ + /* "/" + "COUNTY_POPULATION" +*/ " * " + sales_centers.get(i) + " + ");
        }
        obj_func = obj_func + "0 " + System.getProperty("line.separator") + "  End Equations" + System.getProperty("line.separator") + "End Model";

        //force variables to equal 0 or 1 in optimized result
        while (!(sales_centers.isEmpty())) {
            apmWriter.write("    " + sales_centers.get(0) + " = 0, >=0, <=1" + System.getProperty("line.separator"));
            sales_centers.remove(0);
        }
        apmWriter.write("  End Variables" + System.getProperty("line.separator") + System.getProperty("line.separator") + "  Equations" + System.getProperty("line.separator"));

        //Write constraints to file
        while (!(constraints.isEmpty())) {
            apmWriter.write("    " + constraints.get(0) + System.getProperty("line.separator"));
            constraints.remove(0);
        }

        //Write objective function to file
        apmWriter.write(System.getProperty("line.separator") + "    " + obj_func);
        apmWriter.close();
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
                    String county_name = "";
                    //StringBuilder sb = new StringBuilder();
                    while (null != (tempStr = br.readLine())) {
                        /*if(tempStr.contains("formatted_address")) { //return formatted address
                            sb.append(tempStr + '\n');
                            System.out.println(tempStr);
                            break;
                        }*/
                        if(tempStr.contains("long name")) { //return county name
                            county_name = tempStr.substring(30, (tempStr.length()-2));
                        } else if (tempStr.contains("administrative_area_level_2")) {
                            return county_name;
                        }
                    }
                    //return sb.toString();
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