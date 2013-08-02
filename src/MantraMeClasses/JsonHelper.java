package MantraMeClasses;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import com.cedarsoftware.util.io.JsonWriter;

public class JsonHelper {
	
	public static String mantarasToJason(ArrayList<Mantra> allMantras){
		String allData = "";
		for (Mantra m : allMantras) {		
			allData += JsonWriter.toJson( m ) + "\n";
		}
		return allData;
	}
	
	public static String mantarasToJason(Mantra m){
		String allData = JsonWriter.toJson( m ) + "\n";
		return allData;
	}
	
	/*
	public static ArrayList<Mantra> readMantrasFromFile(String fileName) {
		 ArrayList<Mantra> allMan = new ArrayList<Mantra>();		
		File file = new File(fileName);
	    FileInputStream fis = null;
	    BufferedInputStream bis = null;
	    DataInputStream dis = null;	    
	    try {
	        fis = new FileInputStream(file);

	        bis = new BufferedInputStream(fis);
	        dis = new DataInputStream(bis);

	        while (dis.available() != 0) {
				String line = dis.readLine();
				Mantra m = (Mantra) JsonReader.toJava(line);
				allMan.add(m);
				
	        }
	        fis.close();
	        bis.close();
	        dis.close();
	      } catch (FileNotFoundException e) {
	        e.printStackTrace();
	      } catch (IOException e) {
	        e.printStackTrace();
	      }	    
		return allMan;
	}
	*/
	
	public static void writeToFile(String allData) {
		try {
	    	  FileWriter fstream = new FileWriter("out.txt");
	          BufferedWriter out = new BufferedWriter(fstream);
	          out.write(allData);
	          out.flush();
	          out.close();
	          fstream.close();
	    	} catch (Exception e) {
	    	  e.printStackTrace();    	
	    	}  
	}
}
