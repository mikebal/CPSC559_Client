/**
 * @author Richard Game
 * 
 * Ads and prints out the Trackers information
 */
import java.util.ArrayList;

public class TrackerManager {

    private ArrayList<TrackerInfo> ti;

    public TrackerManager() {
	this.ti = new ArrayList<TrackerInfo>();
    }

    public void addTracker(TrackerInfo t) {
	this.ti.add(t);
    }

    public ArrayList<TrackerInfo> getTrackers() {
	return ti;
    }

    public void printTrackers() {
	for (int i = 0; i < ti.size(); i++) {
	    System.out.println(i + 1 + ".    " + ti.get(i).getGroupName());
	}
    }

}
