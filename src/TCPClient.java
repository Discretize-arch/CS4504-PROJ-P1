import java.io.*;
import java.net.*;

public class TCPClient {
  public static void main(String[] args) throws IOException {

    // Variables for setting up connection and communication
    Socket Socket = null; // socket to connect with ServerRouter
    PrintWriter out = null; // for writing to ServerRouter
    BufferedReader in = null; // for reading form ServerRouter
    InetAddress addr = InetAddress.getLocalHost();
    String host = addr.getHostAddress(); // Client machine's IP
    //Paramaterization by Dillon
    String routerName; 
    if(args.length==2||args.length==1) {
      routerName = args[0]; // ServerRouter host name
    }
    else{
      routerName = "172.20.0.5";
    }
    int SockNum;
    if(args.length==2){
      SockNum = Integer.parseInt(args[1]);
    }
    else {
      SockNum = 5555; // port number
    }

    // Tries to connect to the ServerRouter
    try {
      Socket = new Socket(routerName, SockNum); //Thomas. Broadcasting on port 5555 for routername, trying to make connection to 'routername.'
      out = new PrintWriter(Socket.getOutputStream(), true); //Thomas. A way to send data to the router.
      in = new BufferedReader(new InputStreamReader(Socket.getInputStream())); //Thomas. A way to read data from the router.
    } catch (UnknownHostException e) { //Thomas. There is no router on port 5555 that matches 'routername.'
      System.err.println("Don't know about router: " + routerName);
      System.exit(1);
    } catch (IOException e) {
      System.err.println("Couldn't get I/O for the connection to: " + routerName);
      System.exit(1);
    }

    // Variables for message passing
    Reader reader = new FileReader("file.txt"); 
    BufferedReader fromFile = new BufferedReader(reader); // reader for the string file
    String fromServer; // messages received from ServerRouter
    String fromUser; // messages sent to ServerRouter
    String address = "172.20.0.6"; // destination IP (Server) 
    long t0, t1, t; //Thomas. Variables for time calculation

    // Communication process (initial sends/receives
    out.println(address); // initial send (IP of the destination Server)
    fromServer = in.readLine(); // initial receive from router (verification of connection)
    System.out.println("ServerRouter: " + fromServer);
    out.println(host); // Client sends the IP of its machine as initial send
    t0 = System.currentTimeMillis(); //Thomas. Initial time.

    // Communication while loop
    while ((fromServer = in.readLine()) != null) {
      System.out.println("Server: " + fromServer);
      t1 = System.currentTimeMillis();
      if (fromServer.equals("Bye.")) { // exit statement
        break;
      }
      t = t1 - t0; //Thomas. Cycle Time
      System.out.println("Cycle time: " + t);

      fromUser = fromFile.readLine(); // reading strings from a file
      if (fromUser != null) {
        System.out.println("Client: " + fromUser);
        out.println(fromUser); // sending the strings to the Server via ServerRouter
        t0 = System.currentTimeMillis();
      }
    }

    // closing connections
    out.close();
    in.close();
    Socket.close();
  }
}
