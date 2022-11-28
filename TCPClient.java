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
		String routerName = "XXX.XXX.X.XX"; //### PUT SERVERROUTER COMPUTER NAME OR IP HERE
		int SockNum = 5555; // port number

		// variables or calculations
		long MessagesSent = 0; // count the total # of messages sent
		long TotalMessageSize = 0; // count how many bytes have been sent
		long TotalCycleTime = -1;
		int ConnectionSetupTime = 0;

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
		File file = new File("C:/Users/XXX/XXX"); //### PUT FILE PATH OF FILE TO BE TRANSMITTED HERE
		InputStream inputStream = new FileInputStream(file);
		OutputStream outputStream = Socket.getOutputStream();
		byte[] byteBuffer = new byte[1024]; // holds bytes being sent
		int messageSize = 0; // count message size being sent
		String fromServer; // messages received from ServerRouter
		String address = "XXX.XXX.X.XX";  //### PUT SERVER IP ADDRESS HERE
		long t0, t1 = 0, t;

		// Communication process (initial sends/receives
		out.println(address); // initial send (IP of the destination Server)
		t0 = System.currentTimeMillis();
		fromServer = in.readLine(); // initial receive from router (verification of connection)
		System.out.println("ServerRouter: " + fromServer);
		out.println(host); // Client sends the IP of its machine as initial send

		// wait until connection setup
		while ((fromServer = in.readLine()) != null) {
			System.out.println("Server: " + fromServer);
			fromServer = "";
			break;
		}

		// Communication while loop
		while ((messageSize = inputStream.read(byteBuffer)) > 0) {
			if (TotalCycleTime == -1) {
				t1 = System.currentTimeMillis();
				t = t1 - t0;
				TotalCycleTime = 0;
				ConnectionSetupTime = (int) t;
			}
				MessagesSent++;
				TotalMessageSize += messageSize;
				System.out.println("Total Message Size Sent: " + TotalMessageSize);
				outputStream.write(byteBuffer, 0, messageSize);
		}

		// send at end to signify file is finished sending
		byte DoneSending[] = {'D', 'O', 'N', 'E'}; 
        outputStream.write(DoneSending, 0, 4);

		// wait for the file to be fully received
		while ((fromServer = in.readLine()) != null) {
			System.out.println("Server: " + fromServer);
			break;
		}

		t = System.currentTimeMillis() - t1; // calculate total send/receive time

		// print calculations
		System.out.println("Connection setup time: " + ConnectionSetupTime + 
				"ms.\nTotal message size sent: " + TotalMessageSize + " bytes.\nTotal Messages Sent: "
				+ MessagesSent + ".\nAverage message size sent: " + 
				(TotalMessageSize / MessagesSent) + " bytes.\nTotal time from sending to fully received: "
				 + t + "ms.\nBytes Sent Per ms: " + (TotalMessageSize / t));

		// closing connections
		inputStream.close();
		outputStream.close();
		out.close();
		in.close();
		Socket.close();

		// plays the audio/video file that was sent
		if (! (file.getName().charAt(file.getName().length() - 1) == 't' && 
				file.getName().charAt(file.getName().length() - 2) == 'x' && 
				file.getName().charAt(file.getName().length() - 3) == 't')) {
				ClientApp.run(args);
		}
	}
}
