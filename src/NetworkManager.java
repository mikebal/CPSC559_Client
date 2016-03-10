import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class NetworkManager {

	private Socket connection;
	InputStream is;
	OutputStream os;
	
	public NetworkManager(){
		this.connection = null;
		this.is = null;
		this.os = null;
	}
	
	public void connect(String hostname, int port) throws IOException{
		this.connection = new Socket(hostname,port);
		this.is = connection.getInputStream();
		this.os = connection.getOutputStream();
	}
	
	public void closeConnection() throws IOException{
		this.connection.close();
		this.connection = null;
		
		this.is.close();
		this.is = null;
		
		this.os.close();
		this.os = null;
	}
	
}
