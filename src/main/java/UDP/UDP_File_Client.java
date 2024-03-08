package UDP;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDP_File_Client {
    public static void main(String[] args) {

        try {
            // Open streams for communication
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

            // Get server IP address from user input
            System.out.print("Enter server IP address: ");
            String SERVER_IP = userInput.readLine();

            // Get server port from user input
            System.out.print("Enter server port: ");
            int PORT = Integer.parseInt(userInput.readLine());

            DatagramSocket socket = new DatagramSocket();

            // Open streams for communication
            userInput = new BufferedReader(new InputStreamReader(System.in));

            // Get user input for the file name
            System.out.println("Enter file name: ");
            String filename = userInput.readLine();

            // Convert the filename to bytes
            byte[] sendData = filename.getBytes();

            // Send the filename to the server
            InetAddress serverAddress = InetAddress.getByName(SERVER_IP);
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, PORT);
            socket.send(sendPacket);

            // Receive file from server
            byte[] receiveData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            socket.receive(receivePacket);

            // Write receive data to a file
            FileOutputStream fileOutputStream = new FileOutputStream("received_" + filename);
            fileOutputStream.write(receiveData, 0, receivePacket.getLength());
            fileOutputStream.close();

            System.out.println("File received successfully.");

            // Close socket
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
