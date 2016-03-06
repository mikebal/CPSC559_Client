import java.io.*;
import java.net.*;
import java.util.*;

public class Client {

	public static void main(String[] args) {
		Socket clientSocket = null;
		InputStream is = null;
		OutputStream os = null;

		String id = null;
		if (args.length == 0) {
			id = "JohnDoe";
		} else
			id = args[0];
		ClientInfo ci = new ClientInfo(id);

		// attempt to connect to the server
		try {
			// create a socket
			clientSocket = new Socket("localhost", 9001);

			// get the input and output streams
			os = clientSocket.getOutputStream();
			is = clientSocket.getInputStream();
			Scanner in = new Scanner(is);
			PrintWriter out = new PrintWriter(os, true /* autoflush */);
			Scanner userIn = new Scanner(System.in);

			out.print(ci.retrieveIdentifier() + "#");
			for (String fileName : ci.retrieveFileList())
				out.print(fileName);
			out.println("#joining");

			String userLine = null;
			System.out.println("Enter command to send to the SERVER");
			do {
				userLine = userIn.nextLine();

				String[] input = userLine.split(" ");

				if (input[0].equalsIgnoreCase("get")) {
					System.out.println("Requesting file " + input[1]
							+ " from server");
					out.println(ci.retrieveIdentifier() + "#" + input[0] + "#"
							+ input[1]);
					// do something with client-client connection to retrieve file
				} else if (input[0].equalsIgnoreCase("add")) {
					if (ci.addFile(input[1])) {
						System.out.println("Broadcasting file " + input[1]
								+ " to server");

						out.println(ci.retrieveIdentifier() + "#" + input[0]
								+ "#" + input[1]);
					}
				} else if (!input[0].equalsIgnoreCase("exit")) {
					System.err.println("Unrecognized command");
				}
			} while (!userLine.equalsIgnoreCase("exit"));
			out.println(ci.retrieveIdentifier() + "#leaving");
			userIn.close();
			in.close();
			clientSocket.close();
			System.out.println("Terminating program");

		}

		catch (IOException ioe) {
			System.err
					.println("Couldn't create socket or could not connect to server");
			System.exit(0);
		}
	}
}