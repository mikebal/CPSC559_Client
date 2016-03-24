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

		receivedMSG = in.readLine();

		System.out.println("Client requested file: " + receivedMSG);

		File file = new File(receivedMSG);
		FileInputStream fis = new FileInputStream(file);
		byte[] mybytearray = new byte[(int) file.length()];

		BufferedInputStream bis = new BufferedInputStream(fis);
		bis.read(mybytearray, 0, mybytearray.length);

		out.println(file.length());

		bis = new BufferedInputStream(fis);
		bis.read(mybytearray, 0, mybytearray.length);

		System.out.println("Sending " + receivedMSG + "(" + mybytearray.length + " bytes)");
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