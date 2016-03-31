import java.io.IOException;
import java.util.ArrayList;

public class Parser {

	protected int CLIENTSIZE = 2;
	protected String ARGUMENTSEPERATOR = "'#";

	public Parser() {

	}

	public void parseTracker(TrackerManager tm, String input) {

		String[] parsedRedirectInput = input.split(ARGUMENTSEPERATOR);

		int count = 0;

		while (count < (parsedRedirectInput.length - 1)) {
			TrackerInfo tr = new TrackerInfo(parsedRedirectInput[count]);
			count += 1;

			int trackerCount = Integer.parseInt(parsedRedirectInput[count]);
			for (int i = 0; i < trackerCount; i++) {

				String hostname = parsedRedirectInput[count + 1];

				int port = Integer.parseInt(parsedRedirectInput[count + 2]);

				tr.addLocation(hostname, port);
				count += 2;

			}

			tm.addTracker(tr);
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
				Location l = new Location(parsedClients[offset],
						Integer.parseInt(parsedClients[offset + 1]));
				loc.add(l);
				offset += 2;
			}

			fm.addFile(filename, loc);

		} catch (NullPointerException e) {
			System.err.println("No clients with file");
		}

	}

}
