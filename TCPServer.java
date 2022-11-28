import java.io.*;
import java.net.*;

public class TCPServer {
	public static void main(String[] args) throws IOException {

		// Variables for setting up connection and communication
		Socket Socket = null; // socket to connect with ServerRouter
		PrintWriter out = null; // for writing to ServerRouter
		BufferedReader in = null; // for reading form ServerRouter
		InetAddress addr = InetAddress.getLocalHost();
		String host = addr.getHostAddress(); // Server machine's IP
		String routerName = "XXX.XXX.X.XX"; //### PUT SERVERROUTER PC NAME OR IP HERE
		int SockNum = 5555; // port number

		// Variables to verify transmission
		int messageSize = 0; // calculates how many bytes are received with each message
		long TotalMessageSize = -1; // calculates the total message size received

		// Tries to connect to the ServerRouter
		try {
			Socket = new Socket(routerName, SockNum);
			out = new PrintWriter(Socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(Socket.getInputStream()));
		} catch (UnknownHostException e) {
			System.err.println("Don't know about router: " + routerName);
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to: " + routerName);
			System.exit(1);
		}

		// Variables for message passing
		String fromServer; // messages sent to ServerRouter
		String fromClient; // messages received from ServerRouter
		InputStream inputStream; // reads the output from the ServerRouter
		byte[] byteBuffer = new byte[1024]; // holds the incoming byte stream
		File file = new File("C:/Users/XXX/XXX"); //### PUT PATH TO WHERE FILE SHOULD BE WRITTEN HERE
		OutputStream outputStream = new FileOutputStream(file); 
		String address = "XXX.XXX.X.XX"; //### PUT CLIENT IP ADDRESS HERE

		// Communication process (initial sends/receives)
		out.println(address);// initial send (IP of the destination Client)
		fromClient = in.readLine();// initial receive from router (verification of connection)
		System.out.println("ServerRouter: " + fromClient);
		inputStream = Socket.getInputStream();

		// Communication while loop
			while ((messageSize = inputStream.read(byteBuffer)) > 0) {
				if (messageSize >= 4) {
					if (byteBuffer[messageSize - 4] == 'D' && byteBuffer[messageSize - 3] == 'O' && 
						byteBuffer[messageSize - 2] == 'N' && byteBuffer[messageSize - 1] == 'E') {
						outputStream.write(byteBuffer, 0, messageSize - 4);
						TotalMessageSize += messageSize - 4;
						System.out.println("Total Message Size Received: " + TotalMessageSize);
						break;
					}
				}
				if (TotalMessageSize == -1) {
					TotalMessageSize = 0;
					out.println("Connected to the router.");
				} else {
					TotalMessageSize += messageSize;
					System.out.println("Total Message Size Received: " + TotalMessageSize);
					outputStream.write(byteBuffer, 0, messageSize);
				}
			}

		// acknowledge to the client all data has been received
		out.println("Success.");
			
		// closing connections
		inputStream.close();
		outputStream.close();
		out.close();
		in.close();
		Socket.close();

		// plays the video and audio after being received
		if (! (file.getName().charAt(file.getName().length() - 1) == 't' && 
            file.getName().charAt(file.getName().length() - 2) == 'x' && 
            file.getName().charAt(file.getName().length() - 3) == 't')) {
            ServerApp.run(args);
        }		
	}
}
