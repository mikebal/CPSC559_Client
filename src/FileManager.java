import java.util.ArrayList;

public class FileManager {

	private ArrayList<Files> file;

	public FileManager() {
	}

	public boolean addFile(String filename, Location[] loc) {
		for (Files f : file) {
			if (filename.equals(f.getFilename()))
				return false;
		}
		
		Files temp = new Files(filename, loc[0].getHostname(), loc[0].getPort());
		for (int i = 1; i < loc.length; i++)
			temp.addLocation(loc[i].getHostname(), loc[i].getPort());
		
		return true;
	}
	
	public Files findFile(String filename){
		for (Files f : file){
			if (filename.equals(f.getFilename()))
				return f;
		}	
		return null;
	}

}
