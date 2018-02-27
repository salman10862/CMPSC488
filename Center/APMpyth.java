import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class APMpyth {
	private File file_loc;
	
	
	public APMpyth(String new_path) {
		this.file_loc = new File(new_path);
	}
	
	public String sendData() throws IOException {
		/*
		 * CALL PYTHON SCRIPT
		 * 
		 * AFTER IT RUNS, READ OPTIMIZED RESULTS
		 */
		return(this.readData());
	}
	
	private String readData() throws IOException {
		FileReader f = new FileReader(this.file_loc);
		
		f.close();
		return "";
	}

}
