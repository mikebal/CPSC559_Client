import java.io.IOException;
import java.util.ArrayList;

public class Parser {

    protected int TRACKERSIZE = 4;
    protected int CLIENTSIZE = 2;
    protected String ARGUMENTSEPERATOR = "'#";

    public Parser() {

    }

    public void parseTracker(TrackerManager tm, String input) {
	String[] parsedRedirectInput = input.split(ARGUMENTSEPERATOR);
	int trackerAmount = parsedRedirectInput.length / TRACKERSIZE;
	int offset = 0;
	for (int i = 0; i < trackerAmount; i++) {
	    // create tracker info per group of 4;
	    TrackerInfo tr = new TrackerInfo(parsedRedirectInput[offset]);
	    int tracerlocationAmount = Integer.parseInt(parsedRedirectInput[offset + 1]);
	    // for the second variable (location amount) add to tracker
	    for (int x = 0; x < tracerlocationAmount; x++) {
		tr.addLocation(parsedRedirectInput[offset + 2], Integer.parseInt(parsedRedirectInput[offset + 3]));
		offset += 2;
	    }
	    tm.addTracker(tr);
	    offset++;
	}
    }

    public void parseClients(FileManager fm, String filename, String UserLoc) {

	try {
	    String[] parsedClients = UserLoc.split(ARGUMENTSEPERATOR);
	    ArrayList<Location> loc = new ArrayList<Location>();
	    int clientAmount = parsedClients.length / CLIENTSIZE;

	    int offset = 0;
	    for (int i = 0; i < clientAmount; i++) {
		// create tracker info per group of 4;
		Location l = new Location(parsedClients[offset], Integer.parseInt(parsedClients[offset + 1]));
		loc.add(l);
		offset += 2;
	    }

	    fm.addFile(filename, loc);

	} catch (NullPointerException e) {
	    System.err.println("No clients with file");
	}

    }

}
