import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

public class FileServer {

	public static void main(String[] args) throws Exception, IOException {

		int protocolPort = 3000;

		byte[] receiveData = new byte[1024];
		
		// Open an UDP socket that waits for requests
		
		System.out.println("Server opened");
	
		DatagramSocket socketUDP = new DatagramSocket(protocolPort);
		
		DatagramPacket info = new DatagramPacket(receiveData,
				receiveData.length);
		
		socketUDP.receive(info);
		
		// Datagram received

		InetAddress clientAddress = info.getAddress();
	
		// Parse datagram
		
		String[] data = ( new String(info.getData())).split("\n");
		
		String fileName = data[0];

		int clientport = Integer.parseInt(data[2].trim());
		
		
		
		System.out.println("Request file: " + fileName + " from " + clientAddress
				+ " port " + data[2]);
		
		

		System.out.println("Connection accepted");
		
		socketUDP.close();
		
		// Open a TCP connection to transfer the file

		Socket socketTCP = new Socket(clientAddress, clientport);
		
		
		PrintWriter out = new PrintWriter(socketTCP.getOutputStream(), true);

		FileReader fstream = new FileReader("files_server/"+fileName); 
		
		BufferedReader br = new BufferedReader(fstream);
		
		System.out.println("transfering...");
		
		try {
			
			
			String sCurrentLine;
			 
			while ((sCurrentLine = br.readLine()) != null) {
				System.out.println(sCurrentLine);
				out.println(sCurrentLine);
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		// Close all connections and buffers.
		
		out.close();
		
		System.out.println("Completed. "
				+ "Connection closed.");

		
		socketTCP.close();

	}
}
