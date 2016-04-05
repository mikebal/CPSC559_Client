/**
 * This is the client-side server that other clients connect too in order to retrieve a file requested
 */
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**

 */
public class WorkerRunnable implements Runnable {

    protected Socket clientSocket = null;
    protected String serverText = null;

    public WorkerRunnable(Socket clientSocket, String serverText) {
	this.clientSocket = clientSocket;
	this.serverText = serverText;

    }

    public void run() {

	try {
	    InputStream input = clientSocket.getInputStream();
	    OutputStream output = clientSocket.getOutputStream();
	    String receivedMSG = "";

	    try {
		BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
		// reads in the client request 
		receivedMSG = in.readLine();

		System.out.println("Client requested file: " + receivedMSG);

		File file = new File(receivedMSG);
		FileInputStream fis = new FileInputStream(file);
		byte[] mybytearray = new byte[(int) file.length()];

		BufferedInputStream bis = new BufferedInputStream(fis);
		bis.read(mybytearray, 0, mybytearray.length);
		// tells the requesting client the size of the file 
		out.println(file.length());

		bis = new BufferedInputStream(fis);
		// read the entire file
		bis.read(mybytearray, 0, mybytearray.length);

		
		System.out.println("Sending " + receivedMSG + "(" + mybytearray.length + " bytes)");
		// sends file contents to the requesting client 
		output.write(mybytearray, 0, mybytearray.length);
		output.flush();
		System.out.println("Done.");

	    } catch (IOException e) {
		System.out.println("Read failed");
		System.exit(-1);
	    }
	    long time = System.currentTimeMillis();

	    output.close();
	    input.close();
	    // System.out.println( recevedMSG + " " + time);
	} catch (IOException e) {
	    // report exception somewhere.
	    e.printStackTrace();
	}
    }

}