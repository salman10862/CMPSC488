package pennychain.center;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class JavaApplication3 {
	private int varBound;
	private int max_posi;
	private ArrayList<String> variables = new ArrayList<String>();
	private ArrayList<String> constraints = new ArrayList<String>();
	private map Map;
	
	public JavaApplication3(map new_map) {
		this.Map = new_map;
		varBound = Map.getSize();
	}
	
	public void sendRequest(String path_name) throws IOException {
		this.read_map();
		APMpyth apmpython = new APMpyth(path_name);
		this.bundle_data(path_name);
		
		String results = apmpython.sendData();
		this.display_on_map(results);
	}
	
	private void bundle_data(String path) throws IOException {
		FileWriter f = new FileWriter(path);
		f.write("Model" + System.getProperty("line.separator") + "  Variables" + System.getProperty("line.separator"));
		
		String obj_func = this.create_objective();
		
		while(!(variables.isEmpty())) {
			f.write("    int_" + variables.get(0) + " = 0, >=0, <=1" + System.getProperty("line.separator"));
			variables.remove(0);
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
		String obj_func = "Minimize ";
		for(int i = 0; i < variables.size(); i++) {
			for(int j = 0; j < variables.size(); j++) {
				if(i != j) {
					obj_func = obj_func + Math.sqrt((Math.floor((i-j)/varBound))*(Math.floor((i-j)/varBound)) + ((i-j)%varBound)*((i-j)%varBound)) + " * " + "int_" + variables.get(i) + " + ";
				}
			}
		}
		obj_func = obj_func + "0 " + System.getProperty("line.separator") + "  End Equations" + System.getProperty("line.separator") + "End Model";
		return obj_func;
	}
	
	private void display_on_map(String map) {
		System.out.print(map);
		
		/*
		 * 
		 * DISPLAY MAP DATA ON GUI
		 * 
		 */
	}
	
	private void read_map() throws IOException {
		/*
		 * OBTAIN MAP DATA FROM GUI
		 * 
		 */
                FileWriter f = new FileWriter("C:\\Users\\hgfel\\Documents\\NetBeansProjects\\JavaApplication3\\variables.txt");
		for(int i = 0; i < varBound; i++) {
        	for(int j = 0; j < varBound; j++) {
        		if(Map.map_data[i][j] == 0) {
        			variables.add("X[" + (i*varBound+j) + "]");
        		} else if (Map.map_data[i][j] == 1) {
        			variables.add("X[" + (i*varBound+j) + "]");
        			constraints.add("int_X[" + (i*varBound+j) + "]=1 ");
        		}
                        
                        
        	}
		}
                for(int x = 0; x < variables.size(); x++)
                {
                    f.write("int_" + variables.get(x) + System.getProperty("line.separator"));
                }
                f.close();
                
		String max_num_centers = "";
		for(int i = 0; i < variables.size(); i++) {
			max_num_centers = max_num_centers + "int_" + variables.get(i) + "+";
		}
		constraints.add(max_num_centers + "0=" + max_posi);
                
		
	}
	
	public void setMaxCenters(int desired_max) {
		this.max_posi = desired_max;
	}

}
