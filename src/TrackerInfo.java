/**
 * @author Richard Game
 * 
 *  Contains everything relating to storing a Trackers information
 */
import java.util.ArrayList;

public class TrackerInfo {

    private String groupName;
    private ArrayList<Location> tl = new ArrayList<Location>();
    private int count;

    TrackerInfo(String gn) {
	this.groupName = gn;
	this.count = 0;
    }

    public int returnCount() {
	return this.count;
    }

    public boolean incrementCount() {
	this.count += 1;
	if (tl.size() <= count) {
	    return false;
	}
	return true;
    }

    public void addLocation(String hostname, int port) {
	tl.add(new Location(hostname, port));
    }

    public String getGroupName() {
	return groupName;
    }

    public Location getLocation(int number) {

	if (tl.size() == 1)
	    return tl.get(0);
	else {
	    return tl.get(number);
	}
    }
}
