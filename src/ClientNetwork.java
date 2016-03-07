import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ClientNetwork {

    
    // assumption is made that a client cannot connect to 
    private Socket redirectServer = null;
    private Socket trackerServer = null;
    private Socket clientServer = null;
    private ArrayList<streamHandler> sh = new ArrayList<streamHandler>();
    
    private class streamHandler{
	
	//variable used to keep track of what stream belongs to what host
	private String hAndP = null;
	private InputStream is = null;
	

	private OutputStream os = null;
	
	public streamHandler(){
	    
	}
	
	public void setStreams(InputStream input, OutputStream output, String hostname, int port){
	    this.is = input;
	    this.os = output;
	    this.hAndP = hostname+":"+port;
	}
	
	public void clearStreams() throws IOException{
	    is.close();
	    is = null;
	    os.close();
	    os = null;
	}
	
	public InputStream getIs() {
	    return is;
	}

	public OutputStream getOs() {
	    return os;
	}
	
	
    }
    
    public ClientNetwork(){
    }
    
    public void connectToRedirect(String hostname, int port) throws IOException{
	
	
    }
    
    /**
     * 
     * @param hostname
     * @param port
     * @throws IOException
     * 
     * Function used for Client-to-Tracker connection 
     */
    
    public void connectToTracker(String hostname, int port) throws IOException {
	
    }
    
    /**
     * 
     * @param hostname
     * @param port
     * @throws IOException
     * 
     * Function used for Client-to-Client information exchange
     */
    private void connectToClient(String hostname, int port) throws IOException{
	
    }
    
    public Socket getRedirectServer(){
	return this.redirectServer;
    }
    
    public Socket getTrackerServer(){
	return this.redirectServer;
    }

    public Socket getClientServer(){
	return this.clientServer;
    }
}
