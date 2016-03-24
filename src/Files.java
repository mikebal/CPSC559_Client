import java.util.ArrayList;

public class Files {
    private String filename;
    private ArrayList<Location> loc;
   

    private int count;

    public Files(String filename, String hostname, int port) {
	this.filename = filename;
	this.count = 1;
	this.loc = new ArrayList<Location>();
	this.loc.add(new Location(hostname, port));
    }

    public void addLocation(String hostname, int port) {
	loc.add(new Location(hostname, port));
    }

    public String getFilename() {
	return this.filename;
    }

    public int getClientCount() {
	return this.count;
    }
    
    public ArrayList<Location> getLoc() {
        return loc;
    }

    public boolean incrementCount() {
	count += 1;
	if (count > loc.size()) {
	    count = 0;
	    return false;
	}
	return true;
    }
}
