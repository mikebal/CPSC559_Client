import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

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
			if (getFile(input[1])) {
				System.out.println("file retrieved");
				addFile(input[1]);
			} else
				System.out.println("Unable to retrieve file");

		} else if (input[0].equalsIgnoreCase("add")) {
			addFile(input[1]);

		} else if (input[0].equals("showuserlist")) {
			getUserList(input);

		} else if (input[0].equals("getfilelist")) {
			getFileList(input);

		} else if (input[0].equals("leave")) {

		} else if (!input[0].equalsIgnoreCase("exit")) {
			System.err.println("Unrecognized command");
		}

	}

	private void addFile(String input) throws IOException {
		System.out.println("Broadcasting file " + input + " to server");
		n.out.println("add'#" + input + "'#" + n.IPaddress + "'#" + n.openPort);
	}

	private boolean getFile(String input) {

		String tInput;

		System.out.println("Requesting file " + input + " from server");
		n.out.println("get" + "'#" + input);

		try {
			tInput = n.in.readLine();

			System.out.println(tInput);

			p.parseClients(f, input, tInput);

			boolean retrieved = false;

			Files toGet = f.findFile(input);

			if (toGet.getClientCount() > 0) {

				while (!retrieved || toGet.getClientCount() < toGet.getLoc().size()) {

					Location temp = toGet.getLoc().get(toGet.getClientCount() - 1);

					String host = temp.getHostname();

					int port = temp.getPort();

					try {

						Socket s = new Socket(host, port);

						InputStream is = s.getInputStream();
						OutputStream os = s.getOutputStream();
						PrintWriter out = new PrintWriter(os, true /* autoflush */);
						BufferedReader in = new BufferedReader(new InputStreamReader(is));

						out.println(input);

						String fileInfo = in.readLine();

						int filesize = Integer.parseInt(fileInfo);

						byte[] fileArray = new byte[filesize];

						File f = new File("" + input + "1");

						f.createNewFile();

						FileOutputStream fos = new FileOutputStream("" + input + "1");

						BufferedOutputStream bos = new BufferedOutputStream(fos);
						int bytesRead;
						int current = 0;

						bytesRead = is.read(fileArray, 0, fileArray.length);
						current = bytesRead;

						do {

							bytesRead = is.read(fileArray, current, (fileArray.length - current));
							if (bytesRead >= 0)
								current += bytesRead;
						} while (current < filesize);

						bos.write(fileArray, 0, current);
						bos.flush();
						System.out.println("File " + input + " downloaded (" + current + " bytes read)");

						s.close();
						s = null;
						is.close();
						is = null;
						os.close();
						os = null;
						out.close();
						out = null;
						bos.close();
						bos = null;
						fos.close();
						fos = null;

						retrieved = true;

						return true;

					} catch (IOException e) {
						System.err.println(e);
						System.err.println("trying new client");
						if (!toGet.incrementCount())
							break;
					}
				}
			}

			return false;
		}

		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;

	}

	private void getUserList(String[] inp) throws IOException {
		System.out.println("Asking Tracker for user list");
		n.out.println("show user list");
	}

	private void getFileList(String[] inp) throws IOException {
		System.out.println("Asking Tracker for file list");
		n.out.println("get file list");
		
		String response = n.in.readLine();
		
		String [] parsedResponse = response.split("#'");
		System.out.println("Available files to retrieve:");
		for ( String x : parsedResponse){
			System.out.println(x);
		}
	}

}
