package pennychain.center;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import pennychain.controller.Map;
import pennychain.controller.projResource;

public class OptimizationRequest {
    private int varBound;
    private int max_posi;
    private ArrayList<String> sales_centers = new ArrayList<>();
    private ArrayList<String> dist_centers = new ArrayList<>();
    private ArrayList<String> constraints = new ArrayList<>();
    private Map map;
    private ArrayList<projResource> projectResourceList;


    public OptimizationRequest(ArrayList<projResource> projectResourceList) {
        this.projectResourceList = projectResourceList;
        varBound = (int) projectResourceList.size();
    }

    public void sendRequest(String path_name) throws IOException {
        /*this.read_map(path_name);
        APMpyth apmpython = new APMpyth(path_name);
        this.bundle_data(path_name);

        String results = apmpython.sendData();
        */
        String[] destinations = new String[sales_centers.size()];
        String[] origins = new String[dist_centers.size()];
        int d = 0;
        int o = 0;
        for(int i = 0; i < projectResourceList.size(); i++) {
            if(projectResourceList.get(i).getrType() == 0) {
                destinations[d] = googleReverseGeocode(/*pass in lat/long of this resource*/);
                d++;
            } else if (projectResourceList.get(i).getrType() == 1) {
                origins[o] = googleReverseGeocode(/*pass in lat/long of this resource*/);
                o++;
            }
        }
        //test vars//
        String ori = "40.6655101,-73.89188969999998";
        String des = "40.6905615%2C-73.9976592%7C40.6905615%2C-73.9976592%7C40.6905615%2C-73.9976592%7C40.6905615%2C-73.9976592%7C40.6905615%2C-73.9976592%7C40.6905615%2C-73.9976592%7C40.659569%2C-73.933783%7C40.729029%2C-73.851524%7C40.6860072%2C-73.6334271%7C40.598566%2C-73.7527626%7C40.659569%2C-73.933783%7C40.729029%2C-73.851524%7C40.6860072%2C-73.6334271%7C40.598566%2C-73.7527626";
        //end test vars//

        //googleReverseGeocode();
        System.out.println(googleDistanceMatrix(ori, des));

        //String results = "";
        //return this.display_on_map(results);
    }

    private void bundle_data(String path) throws IOException {
        FileWriter f = new FileWriter(path);
        f.write("Model" + System.getProperty("line.separator") + "  Variables" + System.getProperty("line.separator"));

        String obj_func = this.create_objective();

        while (!(sales_centers.isEmpty())) {
            f.write("    int_" + sales_centers.get(0) + " = 0, >=0, <=1" + System.getProperty("line.separator"));
            sales_centers.remove(0);
        }
        f.write("  End Variables" + System.getProperty("line.separator") + System.getProperty("line.separator") + "  Equations" + System.getProperty("line.separator"));
        while (!(constraints.isEmpty())) {
            f.write("    " + constraints.get(0) + System.getProperty("line.separator"));
            constraints.remove(0);
        }

        f.write(System.getProperty("line.separator") + "    " + obj_func);
        f.close();
    }

    private String create_objective() {
        String obj_func = "Minimize ";
        for (int i = 0; i < sales_centers.size(); i++) {
            obj_func = obj_func + (/*gas_price.get(i)*distance.get(i)-demand.get(i)*/1) + " * int_" + sales_centers.get(i) + " + ";
        }   //TODO: obtain distances from google maps, obtain gas prices from online, calculate demand from census data
        obj_func = obj_func + "0 " + System.getProperty("line.separator") + "  End Equations" + System.getProperty("line.separator") + "End Model";
        return obj_func;
    }

    private Map display_on_map(String map) {
        /*
         *
         * Convert file from APMPyth to Map type to display on GUI
         *
         */
        Map opmap = new Map(this.map.getGrid_size(), this.map.getLength(), this.map.getWidth(), this.map.getZoom(), this.map.getLatitude(), this.map.getLongitude());
        //TODO: populate map_data of opmap using results from apmpython
        return opmap;
    }

    private void read_map(String path) throws IOException {

        for (int i = 0; i < varBound; i++) {
            if (projectResourceList.get(i).getrType() == 0) {
                sales_centers.add("X[" + i + "]");
                constraints.add("X[" + i + "]=0");
            } else if (projectResourceList.get(i).getrType() == 1) {
                dist_centers.add("X[" + i + "]");
                //add contraints pertaining to this variable
            } else {
                sales_centers.add("X[" + i + "]");
                constraints.add("X[" + i + "]=0");
            }
        }

        FileWriter f = new FileWriter(path);

        for (int x = 0; x < sales_centers.size(); x++) {
            f.write("int_" + sales_centers.get(x) + System.getProperty("line.separator"));
        }
        f.close();

        String max_num_centers = "";
        for (int i = 0; i < sales_centers.size(); i++) {
            max_num_centers = max_num_centers + "int_" + sales_centers.get(i) + "+";
        }
        constraints.add(max_num_centers + "0 =" + max_posi);
    }

    public void setMaxCenters(int desired_max) {
        this.max_posi = desired_max;
    }

    private String googleReverseGeocode() {
        try {
            double lat = 40.714224;
            double lng = -73.961452;
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
        } catch (MalformedURLException e) {            // new URL() failed

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
                if(minDistance[j].isEmpty()) {
                    minDistance[j] = currentDistance;
                } else if(Double.parseDouble(minDistance[j]) < Double.parseDouble(currentDistance)) {
                    minDistance[j] = currentDistance;
                }
            }
        }
        return minDistance;
    }
}