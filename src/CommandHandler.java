import java.io.IOException;

public class CommandHandler {

    private NetworkManager n;
    private FileManager f;
    private Parser p;

    public CommandHandler(NetworkManager nm, FileManager fm, Parser parse) {
	this.n = nm;
	this.f = fm;
	this.p = parse;
    }

    public void handleCommand(String[] input) throws IOException {

	if (input[0].equalsIgnoreCase("get")) {
	    getFile(input[1]);

	} else if (input[0].equalsIgnoreCase("add")) {
	    addFile(input[1]);

	} else if (!input[0].equalsIgnoreCase("exit")) {
	    System.err.println("Unrecognized command");
	}

    }

    private void addFile(String input) throws IOException {
	System.out.println("Broadcasting file " + input + " to server");
	n.out.println("add'#" + input + "'#" + n.IPaddress + "'#" + n.openPort);
    }

    private void getFile(String input) throws IOException {

	String tInput;

	System.out.println("Requesting file " + input + " from server");
	n.out.println("get" + "'#" + input);

	tInput = n.in.readLine();

	System.out.println(tInput);

	p.parseClients(f, input, tInput);

    }

    private void getUserList(String[] inp) throws IOException {

    }

    private void getFileList(String[] inp) throws IOException {

    }

}
