package basketball;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class Writer {

	public static void write(HashMap<Integer, Lineup> lineups) {
		
    	ArrayList<String> stringList = new ArrayList<String>();
    	//arrange lineups by time played, add its toString() to a list of Strings
		for(int h = 0; h < lineups.size(); h++) {
			int max = 0;
			int code = 0;
			for(int i : lineups.keySet()) {
				int timePlayed = lineups.get(i).getTime();
				if(timePlayed > max) {
					max = timePlayed;
					code = lineups.get(i).getCode();
				}
			}
			stringList.add(lineups.get(code).toString());
			lineups.remove(code);
		}
	       
    	try{
            String filename = "output.txt";
            Files.write(Paths.get(filename), stringList);
        }catch(IOException ex){
            ex.printStackTrace();
        }
    	
    }
	
}
