/**
 *  @author Richard Game
 *  
 *  The bread and butter of the client program, handled all the possible commands that the user places and deals with the interaction between the client and the trackers 
 *  
 */
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class CommandHandler {

    private NetworkManager n;
    private FileManager f;
    private Parser p;

    public CommandHandler(NetworkManager nm, FileManager fm, Parser parse) {
	this.n = nm;
	this.f = fm;
	this.p = parse;
    }

    /**
     * 
     * @param input
     * 
     * Takes in the command from the Client and determines what to do with it, an unrecognized command doesn't do anything 
     */
    public void handleCommand(String[] input) {
	try {
	    if (input[0].equalsIgnoreCase("get")) {
		if (getFile(input[1])) {
		    System.out.println("file retrieved");

		} else
		    System.out.println("Unable to retrieve file");

	    } else if (input[0].equalsIgnoreCase("add")) {
		addFile(input[1]);

	    } else if (input[0].equals("showuserlist")) {
		getUserList(input);

	    } else if (input[0].equals("getfilelist")) {
		getFileList(input);

	    } else if (input[0].equals("leave")) {
		n.out.println("leave'#" + n.IPaddress + "'#" + n.openPort);
	    } else if (!input[0].equalsIgnoreCase("exit")) {
		System.err.println("Unrecognized command");
	    }
	} catch (IOException e) {

	}

    }

    /**
     * Function used to tell the the Tracker server that the Client wants to add a file, input, to the Tracker server
     * 
     * @param input
     * @throws IOException
     */
    private void addFile(String input) throws IOException {
	System.out.println("Broadcasting file " + input + " to server");
	n.out.println("add'#" + input + "'#" + n.IPaddress + "'#" + n.openPort);
    }

    /**
     * More complicated function, what it does is that given the input parameter, it asks the Tracker server who owns the files
     * if the Tracker knows who own the file it returns a list of clients to connect too. It then attempts to cannot to the client, read the file 
     * and then save the whole file  
     * 
     * @param input
     * @return
     */
    private boolean getFile(String input) {

	String tInput;

	System.out.println("Requesting file " + input + " from server");
	n.out.println("get" + "'#" + input);

	try {
	    tInput = n.in.readLine();

	    
	    //Parse all the clients the tracker returned
	    p.parseClients(f, input, tInput);

	    boolean retrieved = false;

	    Files toGet = f.findFile(input);

	    //If there were more than zero clients
	    if (toGet.getClientCount() > 0) {

		
		// while the file hasn't been retrieved and not all the clients have been tried
		while (!retrieved || toGet.getClientCount() < toGet.getLoc().size()) {

		    Location temp = toGet.getLoc().get(toGet.getClientCount() - 1);

		    String host = temp.getHostname();

		    int port = temp.getPort();

		    try {

			//connect to the client
			Socket s = new Socket(host, port);

			InputStream is = s.getInputStream();
			OutputStream os = s.getOutputStream();
			PrintWriter out = new PrintWriter(os, true /* autoflush */);
			BufferedReader in = new BufferedReader(new InputStreamReader(is));

			out.println(input);

			
			// read in the file size from the client
			String fileInfo = in.readLine();

			int filesize = Integer.parseInt(fileInfo);

			byte[] fileArray = new byte[filesize];

			//create a new file 
			File f = new File("" + input + "1");

			f.createNewFile();

			FileOutputStream fos = new FileOutputStream("" + input + "1");

			BufferedOutputStream bos = new BufferedOutputStream(fos);
			int bytesRead;
			int current = 0;

			bytesRead = is.read(fileArray, 0, fileArray.length);
			current = bytesRead;

			// read the whole file
			do {

			    bytesRead = is.read(fileArray, current, (fileArray.length - current));
			    if (bytesRead >= 0)
				current += bytesRead;
			} while (current < filesize);

			//write to the newly created file 
			bos.write(fileArray, 0, current);
			bos.flush();
			System.out.println("File " + input + " downloaded (" + current + " bytes read)");

			//cleanup
			s.close();
			s = null;
			is.close();
			is = null;
			os.close();
			os = null;
			out.close();
			out = null;
			bos.close();
			bos = null;
			fos.close();
			fos = null;

			retrieved = true;

			return true;

		    } catch (IOException e) {
			System.err.println(e);
			System.err.println("trying new client");
			if (!toGet.incrementCount()) {
			    System.err.println("no more clients to connect to");
			    break;
			}

		    }
		}
	    }

	    return false;
	}

	catch (IOException e) {
	    // TODO Auto-generated catch block
	    // e.printStackTrace();
	}
	return false;

    }

    /**
     * Prints off all the clients and who is connected to the tracker 
     * 
     * @param inp
     * @throws IOException
     */
    private void getUserList(String[] inp) throws IOException {
	System.out.println("Asking Tracker for user list");
	n.out.println("show user list");
	String response = "";
	while (response != null) {
	    response = n.in.readLine();
	    System.out.println(response);
	}
    }

    /**
     * Retrieved the file list from the tracker
     * 
     * @param inp
     * @throws IOException
     */
    private void getFileList(String[] inp) throws IOException {
	System.out.println("Asking Tracker for file list");
	n.out.println("get file list");

	String response = n.in.readLine();

	if (response != null) {
	    String[] parsedResponse = response.split("'#");
	    System.out.println("Available files to retrieve:");
	    int count = 1;
	    for (String x : parsedResponse) {
		System.out.println(count+". "+x);
		count++;
	    }
	}
    }

}
