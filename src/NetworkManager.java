/** 
 * @author Richard Game
 * 
 *  Creates a socket for a client connecting to another client and a client connecting to a the trackers and redirect
 *  
 *  Also handled reading and writing to those sockets and cleaning up as much as it can
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class NetworkManager {

    public Socket connection;
    public InputStream is;
    public OutputStream os;
    public BufferedReader in;
    public PrintWriter out;
    public String IPaddress;
    public int openPort;

    public NetworkManager() {
	this.connection = null;
	this.is = null;
	this.os = null;

	MultiThreadedServer server = new MultiThreadedServer(9000);

	new Thread(server).start();

	try {
	    Thread.sleep(1000);
	} catch (InterruptedException e1) {
	    // TODO Auto-generated catch block
	    e1.printStackTrace();
	}
	openPort = server.getServerPort();

	IPaddress = "";
	try {
	    InetAddress thisIp = InetAddress.getLocalHost();
	    IPaddress = thisIp.getHostAddress();
	} catch (Exception e) {
	    e.printStackTrace();
	}

	System.out.println("Starting client upload/download server at: " + IPaddress + ":" + openPort);

    }

    public void connect(String hostname, int port) throws IOException {
	this.connection = new Socket(hostname, port);
	this.is = connection.getInputStream();
	this.os = connection.getOutputStream();
	this.in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	this.out = new PrintWriter(connection.getOutputStream(), true /* autoflush */);
    }

    public void closeConnection() throws IOException {
	this.connection.close();
	this.connection = null;

	this.is.close();
	this.is = null;

	this.os.close();
	this.os = null;

	this.in.close();
	this.in = null;

	this.out.close();
	this.out = null;
    }

}
