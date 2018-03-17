package pennychain.center;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class APMpyth {
	private File file_loc;
	
	
	public APMpyth(String new_path) {
		this.file_loc = new File(new_path);
	}
	
	public String sendData() throws IOException {
		ProcessBuilder pb = new ProcessBuilder(file_loc + "Optimization.py");
					//This path will change later. Currently is directory of python install, python file name
					//later python will be an executable in a set project directory
		Process p = pb.start();
		return(this.readData());
	}
	
	private String readData() throws IOException {
		FileReader f = new FileReader(this.file_loc);
		
		f.close();
		return "";
	}

}
