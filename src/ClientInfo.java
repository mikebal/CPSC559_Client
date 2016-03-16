import java.util.ArrayList;

public class ClientInfo {

    private ArrayList<String> fileList;

    public ClientInfo() {

	this.fileList = new ArrayList<String>();
    }

    public boolean addFile(String filename) {
	if (this.fileList.contains(filename))
	    return false;
	else {
	    this.fileList.add(filename);
	    return true;
	}
    }

    public ArrayList<String> retrieveFileList() {
	return this.fileList;
    }

}
