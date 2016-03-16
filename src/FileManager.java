import java.util.ArrayList;

public class FileManager {

    private ArrayList<Files> file;

    public FileManager() {
	this.file = new ArrayList<Files>();
    }

    public boolean addFile(String filename, ArrayList<Location> loc) {

	for (int x = 0; x < this.file.size(); x++) {

	    if (filename.equals(file.get(x).getFilename()))
		return false;
	}

	Files temp = new Files(filename, loc.get(0).getHostname(), loc.get(0).getPort());

	for (int i = 1; i < loc.size(); i++)
	    temp.addLocation(loc.get(i).getHostname(), loc.get(i).getPort());

	file.add(temp);
	return true;
    }

    public Files findFile(String filename) {
	for (Files f : file) {
	    if (filename.equals(f.getFilename()))
		return f;
	}
	return null;
    }

}
