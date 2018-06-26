package basketball;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class ReadWebsite {

	public static void main(String[] args) {
		
		//Each Lineup instance contains a HashSet of Strings representing players
		//A lineup of players a,b,c,d,e is the same as a lineup c,d,b,a,e
		//So, to allow for easy finding and organizing,
		//we place a Lineup instance in a HashMap, with keys being the Lineup's Hashset's Hashcode.
		//This works because two Hashsets with the same elements will have equals Hashcodes,
		//regardless of the order the elements are placed
		HashMap<Integer, Lineup> lineups = new HashMap<Integer, Lineup>();
		
		//starting lineup for the game we are interested in
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
		
		Writer.write(lineups);
		
	}
	
	public static HashMap<Integer,Lineup> readLineups (HashMap<Integer,Lineup> lineups, Lineup startersHalf1, Lineup startersHalf2, String urlString){
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
		currentLineup.replaceLineup(startersHalf1);
		
		try {
			URL url = new URL(urlString);
			Scanner s = new Scanner(url.openStream());
			String previousTime = "20:00";
			String newTime = "";
			while(s.hasNext()) {
				String s0 = s.nextLine();
				String playerName = s0.replaceAll("\\s+", "");
				//if the line contains a player on the roster, the next time could indicate a substitution 
				if(roster.contains(playerName)){
					String s1 = s.nextLine();
					if(s1.contains("enters the game")) {
						if(currentLineup.getPlayerNames().size() == 5) {
							//when there is a substitution, must add the correct amount of time played for this specific lineup
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
					else if(s1.contains("goes to the bench")) {
						currentLineup.removePlayer(playerName);
					}
				}
				//start of second half
				else if(s0.contains("prd2") && s0.contains("2nd Half")) {
					newTime = "00:00";
					currentLineup.addTimeSegment(previousTime, newTime);
					putLineup(lineups, currentLineup);
					previousTime = "20:00";
					currentLineup.replaceLineup(startersHalf2);
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
	
	//place a lineup into a large collection of lineups
	public static void putLineup(HashMap<Integer, Lineup> lineups, Lineup currentLineup) {
		if(lineups.containsKey(currentLineup.getCode())) {
			currentLineup.addTimeSegment(lineups.get(currentLineup.getCode()).getTime());
		}
	    Lineup copy = new Lineup(new HashSet<String>(), currentLineup.getTime());
	    copy.replaceLineup(currentLineup);
	    lineups.put(copy.getCode(), copy);
	    currentLineup.resetTime();
	}
	
	//given a line of text, find the part that contains a time, such as "11:14"
	public static String extractTime(String str) {                

	    boolean found = false;
	    char[] ca = str.toCharArray();
	    int index = 0;
	    for(int i = 0; i < ca.length; i++){
	        if(Character.isDigit(ca[i])){
	            index = i - 1;	
	            found = true;
	        } else if(found){
	            break;                
	        }
	    }

	    return str.substring(index, index+5);
	}
}
