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
		String userLine = null;

		MultiThreadedServer server = new MultiThreadedServer(9000);
		new Thread(server).start();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		int openPort = server.getServerPort();

		String IPaddress = "";
		try {
			InetAddress thisIp = InetAddress.getLocalHost();
			IPaddress = thisIp.getHostAddress();
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("Starting client upload/download server at: " + IPaddress + ":" + openPort);

		do {
			try {
				// create a socket
				// connect it to redirect
				clientSocket = new Socket("localhost", 9000);
				// get the input and output streams
				os = clientSocket.getOutputStream();
				is = clientSocket.getInputStream();
				BufferedReader in = new BufferedReader(new InputStreamReader(is));
				PrintWriter out = new PrintWriter(os, true /* autoflush */);
				Scanner userIn = new Scanner(System.in);
				// close connection once tracker information has been delivered
				out.println("New client");
				ArrayList<TrackerInfo> ti = new ArrayList<TrackerInfo>();

				// receive tracker info
				String trackerServerList = in.readLine();
				System.out.println("List of possible tracker servers:");
				// parse tracker info
				String[] parsedRedirectInput = trackerServerList.split("'#");
				int trackerAmount = parsedRedirectInput.length / 4;
				int offset = 0;
				for (int i = 0; i < trackerAmount; i++) {
					// create tracker info per group of 4;
					TrackerInfo tr = new TrackerInfo(parsedRedirectInput[offset]);
					int tracerlocationAmount = Integer.parseInt(parsedRedirectInput[offset + 1]);
					// for the second variable (location amount) add to tracker
					for (int x = 0; x < tracerlocationAmount; x++) {
						tr.addLocation(parsedRedirectInput[offset + 2],
								Integer.parseInt(parsedRedirectInput[offset + 3]));
						offset += 2;
					}

					ti.add(tr);
					offset++;
				}
				clientSocket.close();
				os.close();
				is.close();
				os = null;
				is = null;
				in.close();
				out.close();
				clientSocket = null;

				for (int i = 0; i < ti.size(); i++) {
					System.out.println(i + 1 + ".    " + ti.get(i).getGroupName());
				}

				System.out.println("Please enter the server (integer) that you wish to connect too:");
				userLine = userIn.nextLine();

				int userChoice = Integer.parseInt(userLine);
				userChoice--;

				try {
					TrackerInfo choice = ti.get(userChoice);
					String cachedHostname = choice.getLocation(0).getHostname();
					int cachedPort = choice.getLocation(0).getPort();

					boolean firstConnection = true;

					do {
						System.out.println("Enter command");
						while (!userIn.hasNext()) {

						}
						userLine = userIn.nextLine();
						clientSocket = new Socket(cachedHostname, cachedPort);
						os = clientSocket.getOutputStream();
						is = clientSocket.getInputStream();
						out = new PrintWriter(os, true /* autoflush */);
						in = new BufferedReader(new InputStreamReader(is));

						if (firstConnection) {
							out.println(IPaddress + "'#" + openPort);
							out.println("joining");
							/**
							 * handle users sending multiple add requests for the files that they own
							 */
							firstConnection = false;
						}

						String[] input = userLine.split(" ");

						if (input[0].equalsIgnoreCase("get")) {
							System.out.println("Requesting file " + input[1] + " from server");
							out.println(input[0] + "'#" + input[1]);
							// do something with client-client connection to
							// retrieve
							// file
							System.out.println(in.readLine());
						} else if (input[0].equalsIgnoreCase("add")) {
							if (ci.addFile(input[1])) {
								System.out.println("Broadcasting file " + input[1] + " to server");
								out.println("add'#" + input[1] + "'#"+ IPaddress + "'#" + openPort);
							}
						} else if (!input[0].equalsIgnoreCase("exit")) {
							System.err.println("Unrecognized command");
						}

						clientSocket.close();
						os.close();
						is.close();
						clientSocket = null;
						os = null;
						is = null;

					} while (!userLine.equalsIgnoreCase("exit") || !userLine.equalsIgnoreCase("leave"));
				} catch (IOException ioe) {
					System.err.println("Couldn't connect to tracker");
					System.exit(0);
				}
				System.out.println("Terminating program");
			} catch (IOException e) {
				System.err.println("Unable to connect to tracker sever");
			}
		} while (userLine.equalsIgnoreCase("exit"));
	} // attempt to connect to the server
}