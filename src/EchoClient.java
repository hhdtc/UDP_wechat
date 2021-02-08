import java.io.*;
import java.net.*;
import java.util.*;

public class EchoClient
{
   public static void main(String[] args)
   {
      try
      {
      	 String server, input;
      	 int port;
      	 Scanner cons = new Scanner(System.in);
      	 System.out.print("Enter server name/address: ");
      	 server = cons.nextLine();
      	 System.out.print("Enter server port: ");
      	 port = Integer.valueOf(cons.nextLine());
         Socket s = new Socket(server, port);
         s.setSoTimeout(1000);
         try
         {
            InputStream inStream = s.getInputStream();
            OutputStream outStream = s.getOutputStream();

            Scanner in = new Scanner(inStream);
            PrintWriter out = new PrintWriter(outStream, true /* autoFlush */);
            System.out.println(in.nextLine());
            while (true) {
            	System.out.print("Enter command: ");
      	        input = cons.nextLine();
      	        out.println(input);
      	        System.out.println(in.nextLine());
            }
         }
         catch (NoSuchElementException e) {
         	 System.out.println("The remote server appears to have closed the connection");
         }
         catch (InterruptedIOException exception) {
         	 System.out.println("Connection timeout");
         }
         finally
         {
            s.close();
         }
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
   }
}
