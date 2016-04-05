/**
 * 
 * @author Richard Game
 *
 *	This is a class that holds the most up-to-date values for when a person has succesfully connected to the
 *redirect server along with the tracker servers. 
 *
 *On a new succesful connection these values get updated
 *
 */
public class CachedValues {

    private String redirectHost;
    private int redirectPort;

    private String trackerHost;
    private int trackerPort;

    public CachedValues() {
	this.redirectHost = null;
	this.redirectPort = -1;

	this.trackerHost = null;
	this.trackerPort = -1;
    }

    public void updateRedirect(String hostname, int port) {
	this.redirectHost = hostname;
	this.redirectPort = port;
    }

    public void updateTracker(String hostname, int port) {
	this.trackerHost = hostname;
	this.trackerPort = port;
    }

    public int getRedirectPort() {
	return redirectPort;
    }

    public String getRedirectHost() {
	return redirectHost;
    }

    public String getTrackerHost() {
	return trackerHost;
    }

    public int getTrackerPort() {
	return trackerPort;
    }

}
