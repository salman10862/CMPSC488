package pennychain.center;

import java.io.*;

public class APMpyth {
	private File file_loc;
	
	
	public APMpyth(String new_path) { this.file_loc = new File(new_path); }

	public boolean sendData() throws IOException {
		String testOutput = "";
		String homedir = System.getenv("USERPROFILE");
		String pyPath = "\\AppData\\Local\\Programs\\Python\\Python36\\python.exe";
		ProcessBuilder pb = new ProcessBuilder(homedir + pyPath,"pennychain\\center\\Optimization.py");
		File testFile = new File("TestFile.txt");
		Process p = pb.start();

		System.out.println(".....start python.....");
		BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
		StringBuilder sb = new StringBuilder();
		String line = "";
		while((line = br.readLine()) != null) {
			System.out.println("Py:  " + line);
			sb.append(line);
		}
		return(true);
	}
	
	private String readData() throws IOException {
		FileReader f = new FileReader(this.file_loc);
		f.close();
		return "";
	}

}
