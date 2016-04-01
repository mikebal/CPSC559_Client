import java.io.IOException;
import java.util.ArrayList;

public class Parser {

	protected int CLIENTSIZE = 2;
	protected String ARGUMENTSEPERATOR = "'#";

	public Parser() {

	}

	public void parseTracker(TrackerManager tm, String input){

		String[] parsedRedirectInput = input.split(ARGUMENTSEPERATOR);

		int count = 0;
		boolean parsingPairs = false;
		boolean parsingGroup = true;
		boolean parsingNum = false;
		String hostname = "";
		int num = 0;
		int numSoFar = 1;
		int port = 0;
		TrackerInfo tr = null;

		for(int i = 0; i < parsedRedirectInput.length; i++){
			if(parsingGroup)
			{
				tr = new TrackerInfo(parsedRedirectInput[i]);
				parsingGroup = false;
				parsingNum = true;
			}
			else if(parsingNum){
				num = Integer.parseInt(parsedRedirectInput[i]);
				parsingPairs = true;
				parsingNum = false;
			}
			else if(parsingPairs){
				if(i%2 == 0)
					hostname = parsedRedirectInput[i];
				else
				{
					port = Integer.parseInt(parsedRedirectInput[i]);
					tr.addLocation(hostname, port);

					if(numSoFar == num)
					{
						num = 0;
						numSoFar = 1;
						parsingPairs = false;
						parsingGroup = true;
						tm.addTracker(tr);
					}
					else
						numSoFar = numSoFar + 1;
				}
			}
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
