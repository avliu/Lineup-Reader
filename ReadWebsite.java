package basketball;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class readWebsite2 {

	public static void main(String[] args) {
		HashMap<Integer, Lineup> lineups = new HashMap<Integer, Lineup>();
		
		HashSet<String> starters1set = new HashSet<String>();
		starters1set.add("SMART,IKENNA");
		starters1set.add("HARRIS,JEREMY");
		starters1set.add("JORDAN,DAVONTA");
		starters1set.add("MASSINBURG,CJ");
		starters1set.add("CLARK,WES");
		Lineup starters1 = new Lineup(starters1set);
		
		HashSet<String> starters2set = new HashSet<String>();
		starters2set.add("PERKINS,NICK");
		starters2set.add("HARRIS,JEREMY");
		starters2set.add("JORDAN,DAVONTA");
		starters2set.add("MASSINBURG,CJ");
		starters2set.add("CLARK,WES");
		Lineup starters2 = new Lineup(starters2set);
		
		lineups = readLineups(lineups, starters1, starters1, "http://www.ubbulls.com/sports/mbkb/2017-18/boxscores/20171219_byss.xml?view=plays");
/*		lineups = readLineups(lineups, starters1, starters1, "http://www.ubbulls.com/sports/mbkb/2017-18/boxscores/20171221_pz6q.xml?view=plays");
		lineups = readLineups(lineups, starters1, starters1, "http://www.ubbulls.com/sports/mbkb/2017-18/boxscores/20171228_tpr7.xml?view=plays");
		lineups = readLineups(lineups, starters1, starters1, "http://www.ubbulls.com/sports/mbkb/2017-18/boxscores/20180102_psuw.xml?view=plays");
		lineups = readLineups(lineups, starters1, starters1, "http://www.ubbulls.com/sports/mbkb/2017-18/boxscores/20180106_5oal.xml?view=plays");
		lineups = readLineups(lineups, starters1, starters1, "http://www.ubbulls.com/sports/mbkb/2017-18/boxscores/20180109_66ho.xml?view=plays");
		lineups = readLineups(lineups, starters1, starters1, "http://www.ubbulls.com/sports/mbkb/2017-18/boxscores/20180109_66ho.xml?view=plays");
		lineups = readLineups(lineups, starters1, starters1, "http://www.ubbulls.com/sports/mbkb/2017-18/boxscores/20180116_vo8m.xml?view=plays");
		lineups = readLineups(lineups, starters1, starters1, "http://www.ubbulls.com/sports/mbkb/2017-18/boxscores/20180119_cblt.xml?view=plays");
		lineups = readLineups(lineups, starters1, starters2, "http://www.ubbulls.com/sports/mbkb/2017-18/boxscores/20180123_s431.xml?view=plays");
*/

		
		int totalTime = 0;
		int size = lineups.size();
		for(int h = 0; h < size; h++) {
			int max = 0;
			int code = 0;
			for(int i : lineups.keySet()) {
				int timePlayed = lineups.get(i).getTime();
				if(timePlayed > max) {
					max = timePlayed;
					code = lineups.get(i).getCode();
				}
			}
			System.out.println(lineups.get(code).toString());
			totalTime += lineups.get(code).getTime();
			lineups.remove(code);
		}
		System.out.println(size + ", " + totalTime);
	}
	
	public static HashMap<Integer, Lineup> readLineups(HashMap<Integer, Lineup> lineups, Lineup starters1, Lineup starters2, String name){
		HashSet<String> roster = new HashSet<String>();
		roster.add("REESE,JAMES");
		roster.add("MCRAE,MONTELL");
		roster.add("HARRIS,JEREMY");
		roster.add("GRAVES,JAYVON");
		roster.add("JORDAN,DAVONTA");
		roster.add("MASSINBURG,CJ");
		roster.add("MOULTRIE,COLBY");
		roster.add("CLARK,WES");
		roster.add("CARUTHERS,DONTAY");
		roster.add("AGORIOGE,GABE");
		roster.add("PERKINS,NICK");
		roster.add("BERTRAM,BROCK");
		roster.add("SMART,IKENNA");
		
		Lineup currentLineup = new Lineup(new HashSet<String>());
		currentLineup.replaceLineup(starters1);
		
		try {
			URL url = new URL(name);
			Scanner s = new Scanner(url.openStream());
			String previousTime = "20:00";
			String newTime = "";
			while(s.hasNext()) {
				String s0 = s.nextLine();
				if(s0.contains("prd2") && s0.contains("2nd Half")) {
					newTime = "00:00";
					currentLineup.addTimeSegment(previousTime, newTime);
					putLineup(lineups, currentLineup);
					previousTime = "20:00";
					currentLineup.replaceLineup(starters2);
				}
				for(String player : roster) {
					if(s0.contains(player)) {
						String playerName = s0.replaceAll("\\s+", "");
						String s1 = s.nextLine();
						if(s1.contains("enters the game")) {
							if(currentLineup.getPlayerNames().size() == 5) {
								boolean flag = false;
								while(flag == false){
									   String s2 = s.nextLine();
									   if(s2.contains("time")) {
										   flag = true;
										   newTime = extractTime(s2);
									   }
								}
								currentLineup.addTimeSegment(previousTime, newTime);
								putLineup(lineups, currentLineup);
								previousTime = newTime;
							}
							currentLineup.addPlayer(playerName);
						}
						if(s1.contains("goes to the bench")) {
							currentLineup.removePlayer(playerName);
						}
					}
				}
			}
			newTime = "00:00";
			currentLineup.addTimeSegment(previousTime, newTime);
			putLineup(lineups, currentLineup);
			s.close();
		}
		catch(IOException ex) {
			ex.printStackTrace();
		}
		
		return lineups;
	}
	
	public static void putLineup(HashMap<Integer, Lineup> lineups, Lineup currentLineup) {
		if(lineups.containsKey(currentLineup.getCode())) {
			currentLineup.addTimeSegment(lineups.get(currentLineup.getCode()).getTime());
		}
	    Lineup newLineup = new Lineup(new HashSet<String>(), currentLineup.getTime());
	    newLineup.replaceLineup(currentLineup);
	    lineups.put(newLineup.getCode(), newLineup);
	    currentLineup.resetTime();
	}
	
	public static String extractTime(String str) {                

	    boolean found = false;
	    char[] ca = str.toCharArray();
	    int index = 0;
	    for(int i = 0; i < ca.length; i++){
	        if(Character.isDigit(ca[i])){
	            index = i - 1;	
	            found = true;
	        } else if(found){
	            // If we already found a digit before and this char is not a digit, stop looping
	            break;                
	        }
	    }

	    return str.substring(index, index+5);
	}
}
