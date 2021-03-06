package basketball;

import java.util.HashSet;

public class Lineup {

	//players in this lineup
	private HashSet<String> playerNames;
	//total amount of time in which the lineup was on the court
	private int time;
	
	public Lineup(HashSet<String> names) {
		playerNames = names;
		time = 0;
	}
	
	public Lineup(HashSet<String> names, int timePlayed) {
		playerNames = names;
		time = timePlayed;
	}

	@SuppressWarnings("unchecked")
	public void replaceLineup(Lineup names) {
		playerNames = (HashSet<String>) names.getPlayerNames().clone();
	}
	
	public void addPlayer(String name) {
		playerNames.add(name);
	}
	
	public void removePlayer(String name) {
		playerNames.remove(name);
	}
	
	public int getCode() {
		return playerNames.hashCode();
	}
	
	public HashSet<String> getPlayerNames(){
		return playerNames;
	}
	
	//given two times, finds the amount of time elapsed,
	//and adds it to the time variable
	public void addTimeSegment(String previousTime, String newTime) {
		int previousMinutes = Integer.parseInt(previousTime.substring(0, 2));
		int previousSeconds = Integer.parseInt(previousTime.substring(3, 5)) + previousMinutes*60;
		int newMinutes = Integer.parseInt(newTime.substring(0, 2));
		int newSeconds = Integer.parseInt(newTime.substring(3, 5)) + newMinutes*60;
		time += previousSeconds-newSeconds;
	}
	
	public void addTimeSegment(int newTime) {
		time += newTime;
	}
	
	public int getTime() {
		return time;
	}
	
	public void resetTime() {
		time = 0;
	}
	
	@Override
	public String toString() {
		String answer = "";
		int minutes = time/60;
		String minuteString = Integer.toString(minutes);
		if(minutes<10) {
			minuteString = "0" + minuteString;
		}
		int second = time%60;
		String secondString = Integer.toString(second);
		if(second<10) {
			secondString = "0" + secondString;
		}
		
		answer += (minuteString + ":" + secondString + " ");
		for(String s : playerNames) {
			answer += (s + " ");
		}
		return answer;
	}
	
}
