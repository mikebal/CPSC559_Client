import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
/**

 */
public class WorkerRunnable implements Runnable{

    protected Socket clientSocket = null;
    protected String serverText   = null;
 

    public WorkerRunnable(Socket clientSocket, String serverText) {
        this.clientSocket = clientSocket;
        this.serverText   = serverText;

    }

    public void run() {

        try {
            InputStream input  = clientSocket.getInputStream();
            OutputStream output = clientSocket.getOutputStream();
            String receivedMSG = "";

            try{
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),
                        true);

                receivedMSG = in.readLine();
                System.out.println("New Connection: " + receivedMSG);

                if(receivedMSG.equals("New Server"))
                {
                    output.write("READY FOR SERVER INFO\n".getBytes());

                    receivedMSG = in.readLine();
                    System.out.println("New server info: " + receivedMSG);
                    while(receivedMSG == null)
                        receivedMSG = in.readLine();

//                    Tracker newServer = new Tracker(receivedMSG);
//                    ServerManager serverManager = new ServerManager(trackerList);
//                    serverManager.addServerToList(newServer);
                }
                else
                {
                    System.out.println("Sending Server list to (" + receivedMSG + ")");
                  //  String trackerString = getServerListString(trackerList);
                 //   out.println(trackerString);
                }

            } catch (IOException e) {
                System.out.println("Read failed");
                System.exit(-1);
            }
            long time = System.currentTimeMillis();

            output.close();
            input.close();
           // System.out.println( recevedMSG + "      " + time);
        } catch (IOException e) {
            //report exception somewhere.
            e.printStackTrace();
        }
    }
 
}