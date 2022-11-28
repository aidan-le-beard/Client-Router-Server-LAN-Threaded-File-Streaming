import java.io.*;
import java.net.*;

public class SThread extends Thread {
	private Object[][] RTable; // routing table
	private PrintWriter out, outTo; // writers (for writing back to the machine and to destination)
	private BufferedReader in; // reader (for reading from the machine connected to)
	private InputStream inputStream; // reads the stream of bytes from the client
	private String inputLine, outputLine, destination, addr; // communication strings
	private Socket outSocket; // socket for communicating with a destination
	private int ind; // indext in the routing table
	private byte[] byteBuffer = new byte[1024]; // buffer to read bytes from the file being sent
	private int messageSize = 0; // count the message size being received
	private OutputStream outputStream; // to send the bytes received to the server
	private int TotalMessageSize = -1; // counts the total number of bytes received

	// Constructor
	SThread(Object[][] Table, Socket toClient, int index) throws IOException {
		out = new PrintWriter(toClient.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(toClient.getInputStream()));
		inputStream = toClient.getInputStream();
		RTable = Table;
		addr = toClient.getInetAddress().getHostAddress();
		RTable[index][0] = addr; // IP addresses
		RTable[index][1] = toClient; // sockets for communication
		ind = index;
	}

	// Run method (will run for each machine that connects to the ServerRouter)
	public void run() {
		try {
			// Initial sends/receives
			destination = in.readLine(); // initial read (the destination for writing)
			System.out.println("Forwarding to " + destination);
			out.println("Connected to the router."); // confirmation of connection

			// waits 10 seconds to let the routing table fill with all machines' information
			try {
				Thread.currentThread().sleep(10000);
			} catch (InterruptedException ie) {
				System.out.println("Thread interrupted");
			}

			// loops through the routing table to find the destination
			for (int i = 0; i < 10; i++) {
				if (destination.equals((String) RTable[i][0])) {
					outSocket = (Socket) RTable[i][1]; // gets the socket for communication from the table
					System.out.println("Found destination: " + destination);
					outTo = new PrintWriter(outSocket.getOutputStream(), true); // assigns a writer
					outputStream = outSocket.getOutputStream();
				}
			}

			// Communication loop for server --> client
			if (ind % 2 == 0) {
				while ((inputLine = in.readLine()) != null) {
					System.out.println("Client/Server said: " + inputLine);
				
					outputLine = inputLine; // passes the input from the machine to the output string for the destination
					if (inputLine == "Success") {
						out.println("Success");
					}
					outTo.println(outputLine); // writes to the destination
					
				} // end while
			// Communication loop for client --> server
			} else if (ind % 2 == 1) {
				while ((messageSize = inputStream.read(byteBuffer)) > 0) {
					// exit statement
					if (messageSize >= 4) {
						if (byteBuffer[messageSize - 4] == 'D' && byteBuffer[messageSize - 3] == 'O' && 
                 			byteBuffer[messageSize - 2] == 'N' && byteBuffer[messageSize - 1] == 'E') {
            	      		outputStream.write(byteBuffer, 0, messageSize - 4);
							TotalMessageSize += messageSize - 4;
							System.out.println("Total Message Size Forwarded: " + TotalMessageSize);
             	    		break;
						}
         	       }
					if (TotalMessageSize == -1) {
						TotalMessageSize = 0;
						outputStream.write(0);
					} else {
						TotalMessageSize += messageSize;
						System.out.println("Total Message Size Forwarded: " + TotalMessageSize);
						outputStream.write(byteBuffer, 0, messageSize); // writes to the destination
					}
					
				} // end while
				byte DoneSending[] = {'D', 'O', 'N', 'E'}; // send when file is finished sending to exit while loop on server
				outputStream.write(DoneSending, 0, 4);
			}
			
		} // end try
		catch (IOException e) {
			System.err.println("Could not listen to socket.");
			System.exit(1);
		}
	}
}