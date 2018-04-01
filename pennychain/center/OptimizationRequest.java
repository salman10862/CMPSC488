package pennychain.center;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
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
	
	public Map sendRequest(String path_name) throws IOException {
		this.read_map(path_name);
		APMpyth apmpython = new APMpyth(path_name);
		this.bundle_data(path_name);
		
		String results = apmpython.sendData();
		return this.display_on_map(results);
	}
	
	private void bundle_data(String path) throws IOException {
		FileWriter f = new FileWriter(path);
		f.write("Model" + System.getProperty("line.separator") + "  Variables" + System.getProperty("line.separator"));
		
		String obj_func = this.create_objective();
		
		while(!(sales_centers.isEmpty())) {
			f.write("    int_" + sales_centers.get(0) + " = 0, >=0, <=1" + System.getProperty("line.separator"));
			sales_centers.remove(0);
		}
		f.write("  End Variables" + System.getProperty("line.separator") + System.getProperty("line.separator") + "  Equations" + System.getProperty("line.separator"));
		while(!(constraints.isEmpty())) {
			f.write("    " + constraints.get(0) + System.getProperty("line.separator"));
			constraints.remove(0);
		}
		
		f.write(System.getProperty("line.separator") + "    " + obj_func);
		f.close();
	}
	
	private String create_objective() {
	    /*
	    New objective format:
	    Minimize Z = (supply cost - demand value)(1 or 0) + ...
	    where each supply cost = price of maintaining supply at this location (gas price * distance)
	               demand value = expected value this location creates (population * target demographic weighting value)
	     */
		String obj_func = "Minimize ";
		for(int i = 0; i < sales_centers.size(); i++) {
		    obj_func = obj_func + (/*gas_price.get(i)*distance.get(i)-demand.get(i)*/1) + " * int_" +sales_centers.get(i) + " + ";
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

	    //take variables from project resource list rather than map (from map is commented out below this)
	    for(int i = 0; i < varBound; i++) {
	        if(projectResourceList.get(i).getrType() == 0) {
                sales_centers.add("X[" + i + "]");
                constraints.add("X[" + i + "]=0");
            } else if(projectResourceList.get(i).getrType() == 1) {
                dist_centers.add("X[" + i + "]");
                //add contraints pertaining to this variable
            } else {
                sales_centers.add("X[" + i + "]");
                constraints.add("X[" + i + "]=0");
            }
        }

		FileWriter f = new FileWriter(path);
		/*
	    for(int i = 0; i < varBound; i++) {
        		for(int j = 0; j < varBound; j++) {
        			if(map.map_data[i][j] == 0) {
        				variables.add("X[" + (i*varBound+j) + "]");
        			} else if (map.map_data[i][j] == 1) {
        				variables.add("X[" + (i*varBound+j) + "]");
        				constraints.add("X[" + (i*varBound+j) + "]=1");
        			}
        		}
		}
		*/
		for(int x = 0; x < sales_centers.size(); x++) {
			f.write("int_" + sales_centers.get(x) + System.getProperty("line.separator"));
		}
		f.close();
		
		String max_num_centers = "";
		for(int i = 0; i < sales_centers.size(); i++) {
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
            HttpURLConnection myURLConnection =(HttpURLConnection) myURL.openConnection();
            myURLConnection.connect();
            int status = myURLConnection.getResponseCode();


            switch(status) {
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(myURLConnection.getInputStream()));
                    String tempStr = "";
                    StringBuilder sb = new StringBuilder();
                    while (null != (tempStr = br.readLine())) {
                        sb.append(tempStr + '\n');
                    }
                    return sb.toString();
            }
        }
        catch (MalformedURLException e) {
            // new URL() failed
            // ...
        }
        catch (IOException e) {
            // openConnection() failed
            // ...
        }
        return null;
    }

}
