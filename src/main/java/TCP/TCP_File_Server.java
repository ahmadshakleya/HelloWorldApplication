package TCP;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

// https://heptadecane.medium.com/file-transfer-via-java-sockets-e8d4f30703a5
// https://www.geeksforgeeks.org/socket-programming-in-java/

public class TCP_File_Server {
    public static void main(String[] args) {
        final int PORT = 5000;

        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server started. Waiting for connections...");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected: " + socket);

                // Create a new thread to handle the client
                Thread t = new Thread(new ClientHandler(socket));
                t.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ClientHandler implements Runnable {
    private Socket socket = null;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            // Open streams for communication
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            String fileName = in.readLine(); // Read the requested file name from client

            // Check if the requested file exists
            File file = new File(fileName);
            if (file.exists() && !file.isDirectory()) {
                // Send file to the client
                FileInputStream fileInputStream = new FileInputStream(file);
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                    socket.getOutputStream().write(buffer, 0, bytesRead);
                }
                fileInputStream.close();
            } else {
                out.println("File not found.");
            }

            // Close connections
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}