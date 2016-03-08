import java.util.ArrayList;


public class TrackerInfo {

	private String groupName;
	private ArrayList<trackerLocations> tl = new ArrayList<trackerLocations>();
	
	class trackerLocations{
		public String hostName;
		public int port;	
		
		trackerLocations(String hostname, int port){
			this.hostName = hostname;
			this.port =  port;
		}
	}
	private String hostName;
	private int port;
	
	TrackerInfo(String gn){
		this.groupName = gn;
	}
	
	public void addLocation(String hostname, int port){
		tl.add(new trackerLocations(hostname, port));
	}
	
	public String getGroupName() {
		return groupName;
	}
	
	public trackerLocations getLocation(int number){
		
		if (tl.size() == 1)
			return tl.get(0);
		else{
			return tl.get(number);
		}
	}
}
