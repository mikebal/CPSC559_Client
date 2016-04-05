/**
 *  @author Richard Game
 *  
 *  Main entry point for the Client program; it creates a client server, connects to the redirect server, 
 *  gets user's decision, and then loops commands over the tracker server until a user explicitly says they aren't
 */
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
		//Connects to the Redirect Server
		nm.connect(args[0], Integer.parseInt(args[1]));

		tm = null;
		tm = new TrackerManager();
		cached.updateRedirect(args[0], Integer.parseInt(args[1]));

		nm.out.println("New client");

		//Reads in the list of trackers from the redirect server
		String trackerServerList = nm.in.readLine();

		//Parses the list
		parse.parseTracker(tm, trackerServerList);

		nm.closeConnection();

		tm.printTrackers();

		System.out.println("Please enter the server (integer) that you wish to connect too:");
		//Scanner userIn = new Scanner(System.in);
		while(!userIn.hasNextLine());
		userLine = userIn.nextLine();

		int userChoice = Integer.parseInt(userLine);
		userChoice--;

		//Chooses the appropriate tracker for user input 
		TrackerInfo choice = tm.getTrackers().get(userChoice);
		boolean firstConnection = true;
		
		//Until user types in leave or exit, loop
		do {
		    try {

		    userLine = "";	
		    	
			cached.updateTracker(choice.getLocation(choice.returnCount()).getHostname(),
				choice.getLocation(choice.returnCount()).getPort());

			
			
			// on a new connection, notify tracker that its a new client
			if (firstConnection) {
			    nm.connect(cached.getTrackerHost(), cached.getTrackerPort());
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


			
			
			System.out.println("Enter command");
			while (!userIn.hasNext()) {

			}

			userLine = userIn.nextLine();

			String[] input = userLine.split(" ");

			//Connect to the Tracker and handle the user input along with the server response
			nm.connect(cached.getTrackerHost(), cached.getTrackerPort());
			
			ch.handleCommand(input);

			nm.closeConnection();

			}
			
		
			// Error checking to see if there is a socket issue connecting with the tracker
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