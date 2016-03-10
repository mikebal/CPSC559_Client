
public class Location {
		private String hostName;
		private int port;	
		
		Location(String hostname, int port){
			this.hostName = hostname;
			this.port =  port;
		}
		
		public String getHostname(){
			return this.hostName;
		}
		
		public int getPort(){
			return this.port;
		}
	
}
