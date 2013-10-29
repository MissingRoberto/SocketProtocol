import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientSocket {

	public static void main(String[] args) throws UnknownHostException,
			IOException {

		// Data for the connection and the protocol 
		
		int protocolPort = 3000;
		int portNumber = 2000;
		String fileName = "file.txt";
		String clientAddress = "localhost";
		String serverHostname = "127.0.0.1";
		String path = "files_client/";

		System.out.println("Request " + fileName + " from "
				+ serverHostname + " port " + portNumber);

		
		// Create two sockets.
		
		DatagramSocket clientUDP = new DatagramSocket();
		ServerSocket socketTCP = new ServerSocket(portNumber);
		

		// Send the request datagram by the UDP connection.

		byte[] sendDataUDP = new byte[1024];
	
		String message = fileName + "\n" +clientAddress + "\n"
				+ portNumber;

		sendDataUDP = message.getBytes();

		InetAddress IPAddress = InetAddress.getByName(serverHostname);
		
		DatagramPacket sendPacket = new DatagramPacket(sendDataUDP,
				sendDataUDP.length, IPAddress, protocolPort);

		System.out.println("Sending control info");

		clientUDP.send(sendPacket);
		
		// close UDP connection
		
		clientUDP.close();
		
		// Wait for the file
		
		System.out.println("Waiting for response");
		
		Socket server = socketTCP.accept();
		
		// the transfer starts
		
		System.out.println("transfering...");

		BufferedReader in = new BufferedReader(new InputStreamReader(
				server.getInputStream()));

		String inputLine;

		FileWriter out = new FileWriter(path + fileName);

		while ((inputLine = in.readLine()) != null) {

			out.write(inputLine + "\n");

		}
		
		// File received and we close all the buffers and connections.
		
		in.close();
		out.close();

	
		System.out.println("File received");
		
		socketTCP.close();

	}
}
