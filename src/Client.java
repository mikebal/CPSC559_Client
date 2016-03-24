import java.io.*;
import java.util.*;

public class Client {

    public static void main(String[] args) {
	TrackerManager tm = new TrackerManager();
	Parser parse = new Parser();
	NetworkManager nm = new NetworkManager();
	FileManager fm = new FileManager();

	String userLine = null;
	CachedValues cached = new CachedValues();
	CommandHandler ch = new CommandHandler(nm, fm, parse);
	
	do {
	    try {
		nm.connect(args[0], Integer.parseInt(args[1]));

		cached.updateRedirect(args[0], Integer.parseInt(args[1]));

		nm.out.println("New client");

		String trackerServerList = nm.in.readLine();

		parse.parseTracker(tm, trackerServerList);

		nm.closeConnection();

		tm.printTrackers();

		System.out.println("Please enter the server (integer) that you wish to connect too:");
		Scanner userIn = new Scanner(System.in);
		userLine = userIn.nextLine();

		int userChoice = Integer.parseInt(userLine);
		userChoice--;

		TrackerInfo choice = tm.getTrackers().get(userChoice);
		boolean firstConnection = true;
		do {
		    try {

			cached.updateTracker(choice.getLocation(choice.returnCount()).getHostname(),
				choice.getLocation(choice.returnCount()).getPort());

			System.out.println("Enter command");
			while (!userIn.hasNext()) {

			}

			userLine = userIn.nextLine();
			nm.connect(cached.getTrackerHost(), cached.getTrackerPort());

			if (firstConnection) {
			    nm.out.println(nm.IPaddress + "'#" + nm.openPort);
			    nm.out.println("joining");
			    firstConnection = false;
			}

			String[] input = userLine.split(" ");

			ch.handleCommand(input);

			nm.closeConnection();

		    } catch (IOException ioe) {
			System.err.println("Couldn't connect to tracker, trying next in group");
			if (!choice.incrementCount()) {
			    System.err.println("No more trackers in group");
			    break;
			}
		    }
		} while (!userLine.equalsIgnoreCase("exit") || !userLine.equalsIgnoreCase("leave"));
		userIn.close();
		System.out.println("Terminating program");
	    } catch (IOException e) {
		System.err.println("Unable to connect to tracker sever");
	    }

	} while (userLine.equalsIgnoreCase("exit"));

    }
}