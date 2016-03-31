import java.io.*;
import java.util.*;

public class Client {

    public static void main(String[] args) {
	TrackerManager tm;
	Parser parse = new Parser();
	NetworkManager nm = new NetworkManager();
	FileManager fm = new FileManager();
	Scanner userIn = new Scanner(System.in);

	String userLine = null;
	CachedValues cached = new CachedValues();
	CommandHandler ch = new CommandHandler(nm, fm, parse);
	
	do {
	    try {
		nm.connect(args[0], Integer.parseInt(args[1]));

		tm = null;
		tm = new TrackerManager();
		cached.updateRedirect(args[0], Integer.parseInt(args[1]));

		nm.out.println("New client");

		String trackerServerList = nm.in.readLine();

		parse.parseTracker(tm, trackerServerList);

		nm.closeConnection();

		tm.printTrackers();

		System.out.println("Please enter the server (integer) that you wish to connect too:");
		//Scanner userIn = new Scanner(System.in);
		userLine = userIn.nextLine();

		int userChoice = Integer.parseInt(userLine);
		userChoice--;

		TrackerInfo choice = tm.getTrackers().get(userChoice);
		boolean firstConnection = true;
		do {
		    try {

		    userLine = "";	
		    	
			cached.updateTracker(choice.getLocation(choice.returnCount()).getHostname(),
				choice.getLocation(choice.returnCount()).getPort());

			
			nm.connect(cached.getTrackerHost(), cached.getTrackerPort());

			if (firstConnection) {
			    nm.out.println("new'#"+nm.IPaddress + "'#" + nm.openPort+"'#");
			    firstConnection = false;
			    while(!nm.in.ready());
			    String serverReply = nm.in.readLine();
			    nm.closeConnection();
			    if(serverReply.equals("refuse"))
			    {
			    	throw new IOException();
			    }
			    else
			    	System.out.println("Successful connection to tracker");
			    userLine = "";
			}
			else{


			
			//nm.connect(cached.getTrackerHost(), cached.getTrackerPort());
			System.out.println("Enter command");
			while (!userIn.hasNext()) {

			}

			userLine = userIn.nextLine();

			String[] input = userLine.split(" ");

			ch.handleCommand(input);

			nm.closeConnection();

			}
			
		

		    } catch (IOException ioe) {
			System.err.println("Couldn't connect to tracker, trying next in group");
			if (!choice.incrementCount()) {
			    System.err.println("No more trackers in group");
			    break;
			}
			else{
				firstConnection = true;
			}
		    }
		   
		} while (!userLine.equalsIgnoreCase("exit") && !userLine.equalsIgnoreCase("leave"));
		
		
	    } catch (IOException e) {
		System.err.println("Unable to connect to tracker sever");
	    }
	    System.out.println("Leaving tracker");
	} while (!userLine.equalsIgnoreCase("exit"));
	System.out.println("Terminating program");
	userIn.close();
	System.exit(0);
    }
}