import java.util.ArrayList;

public class ClientInfo {

	private String identifier;
	private ArrayList<String> fileList;

	public ClientInfo(String identifier) {
		this.identifier = identifier;
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

	public String retrieveIdentifier() {
		return this.identifier;
	}

}
