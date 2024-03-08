package UDP;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDP_File_Server {
    public static void main(String[] args) {
        final int PORT = 5000;

        try {
            DatagramSocket serverSocket = new DatagramSocket(PORT);
            System.out.println("Server started. Waiting for connections...");

            while(true) {
                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);

                System.out.println("Client connected: " + receivePacket.getAddress() + ":" + receivePacket.getPort());

                // Create a new Thread to handle the client
                Thread t = new Thread(new ClientHandler(serverSocket, receivePacket));
                t.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ClientHandler implements Runnable {
    private DatagramSocket serverSocket;
    private DatagramPacket receivePacket;

    public ClientHandler(DatagramSocket serverSocket, DatagramPacket receivePacket) {
        this.serverSocket = serverSocket;
        this.receivePacket = receivePacket;
    }

    public void run() {
        try {
            // Extracting the requested file name from the received packet
            String fileName = new String(receivePacket.getData()).trim();

            // Check if the requested file exists
            File file = new File(fileName);
            if (file.exists() && !file.isDirectory()) {
                // Send file to the client
                FileInputStream fileInputStream = new FileInputStream(file);
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                    DatagramPacket sendPacket = new DatagramPacket(buffer, bytesRead, receivePacket.getAddress(), receivePacket.getPort());
                    serverSocket.send(sendPacket);
                }
                fileInputStream.close();
            } else {
                // Sending file not found message
                String message = "File not found.";
                DatagramPacket sendPacket = new DatagramPacket(message.getBytes(), message.getBytes().length, receivePacket.getAddress(), receivePacket.getPort());
                serverSocket.send(sendPacket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}