# Basketball
University at Buffalo Men's Basketball Team data analytics


Some data analysis that I do involve Synergy. Synergy allows you to filter stats by lineup, but you must input those lineups manually. This project reads a play-by-play log on our team website (example: http://www.ubbulls.com/sports/mbkb/2017-18/boxscores/20171129_3zjs.xml?view=plays) and writes out each lineup and the amount of time in which that lineup was on the court. I can then input the most used lineups to find desired stats on Synergy. I can find lineups and their on-court times for any number of games.

Lineup- class representing each lineup. Contains a set of players and the amount of time in which that set of players has been on the court. Includes methods that allow you to add a player, remove a player, add a time, and convert an instance into a readable String.

ReadWebsite- class containing main method. Contains a method to read the HTML form of the website line by line, looking for events of substitutions, and updating a HashMap of lineups accordingly.

Writer- class that writes a HashMap of lineups to a text file, in order of time played. 
