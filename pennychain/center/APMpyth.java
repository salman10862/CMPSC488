package pennychain.center;

import java.io.*;

public class APMpyth {
	private File file_loc;
	
	
	public APMpyth(String new_path) {
		this.file_loc = new File(new_path);
	}
	
	public String sendData() throws IOException {
		ProcessBuilder pb = new ProcessBuilder("pennychain\\center\\Optimization.exe", "pennychain\\center\\Optimization.py");
		Process p = pb.start();
		BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
		System.out.println(".....start python.....");
		String line = "";
		while((line = br.readLine()) != null) {
			System.out.println("Py:  " + line);
		}
		return(this.readData());
	}
	
	private String readData() throws IOException {
		FileReader f = new FileReader(this.file_loc);
		
		f.close();
		return "";
	}

}
