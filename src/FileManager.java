
/**
 * @author Richard Game
 * 
 * Deals with the individual files and adding and retrieving the files 
 * 
 */
import java.util.ArrayList;

public class FileManager {

    private ArrayList<Files> file;

    public FileManager() {
	this.file = new ArrayList<Files>();
    }

    /**
     * adds a new file and all there locations to the files list, if it cant add
     * the file (because it was already there) it returns false otherwise
     * returns true
     * 
     * @param filename
     * @param loc
     * @return
     */
    public boolean addFile(String filename, ArrayList<Location> loc) {

	for (int x = 0; x < this.file.size(); x++) {

	    if (filename.equals(file.get(x).getFilename()))
		return false;
	}

	if (loc.size() > 0) {
	    Files temp = new Files(filename, loc.get(0).getHostname(), loc.get(0).getPort());

	    for (int i = 1; i < loc.size(); i++)
		temp.addLocation(loc.get(i).getHostname(), loc.get(i).getPort());

	    file.add(temp);
	    return true;
	}
	return false;
    }

    /**
     * Function to find a file
     * 
     * @param filename
     * @return
     */

    public Files findFile(String filename) {
	for (Files f : file) {
	    if (filename.equals(f.getFilename()))
		return f;
	}
	return null;
    }

}
