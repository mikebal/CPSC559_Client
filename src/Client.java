import java.io.*;
import java.net.*;
import java.util.*;
public class Client
{
    public Client()
    {

    }

    public static void main(String[] args)
    {
        Socket  clientSocket = null;
        InputStream     is           = null;
        OutputStream    os           = null;

        //attempt to connect to the server
        try
        {
            //create a socket
            clientSocket = new Socket("localhost", 9000);
            try
            {
                //get the input and output streams
                os = clientSocket.getOutputStream();
                is = clientSocket.getInputStream();
                Scanner in = new Scanner(is);
                PrintWriter out  = new PrintWriter(os, true /* autoflush */);
                Scanner userIn = new Scanner(System.in);

               // String userLine = "";
               // System.out.println("Enter data to send to the SERVER");
               // while(userIn.hasNextLine())
               // {
               //     userLine = userIn.nextLine();
                    out.println("This is Dog");
                 //   out.flush();
                //}

                int serverCount = 0, count = 0;
                System.out.println("Server " + count + ": ");
                while(in.hasNextLine())
                {
                    if(count == 5) {
                        serverCount++;
                        System.out.println("Server " + serverCount + ": ");

                        count = 0;
                    }
                    String line = in.nextLine();
                    count++;
                    System.out.println(line);
                }
            }
            finally
            {
                clientSocket.close();
            }

        }
        catch(IOException ioe)
        {
            System.err.println("Couldn't connect to server");
            System.exit(0);
        }
    }
}