// A Java program for a Server
import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

// https://www.geeksforgeeks.org/socket-programming-in-java/
// https://www.geeksforgeeks.org/transfer-the-file-client-socket-to-server-socket-in-java/
// https://www.w3schools.com/java/java_files_read.asp
// https://stackoverflow.com/a/40255184

public class TCP_Server {
    //initialize socket and input stream
    private ServerSocket server  = null;

    // constructor with port
    public TCP_Server(int port) {
        // starts server and waits for a connection
        try {
            server = new ServerSocket(port);
            server.setReuseAddress(true);
            System.out.println("Server started on port " + port);

            System.out.println("Waiting for a client ...");

            // reads message from client until "close" is sent
            while (true) {
                // socket object to receive incoming client
                // requests
                Socket client = server.accept();

                // Displaying that new client is connected
                // to server
                System.out.println("New client connected " + client.getInetAddress().getHostAddress());

                // create a new thread object
                ClientHandler clientSock = new ClientHandler(client);

                // This thread will handle the client
                // separately
                Thread thread = new Thread(clientSock);
                thread.start();
            }
        } catch(IOException i) { System.out.println("Error 1"); }
    }
    public static void main(String args[]) {
        TCP_Server server = new TCP_Server(5000);
    }

    // ClientHandler class
    private static class ClientHandler implements Runnable {
        private Socket clientSocket = null;
        private DataInputStream in = null;
        private DataOutputStream out = null;
        private volatile boolean isRunning = true;

        // Constructor
        public ClientHandler(Socket socket) { this.clientSocket = socket; }

        public void run() {
            try {
                // takes input from the client socket
                in = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
                out = new DataOutputStream(clientSocket.getOutputStream());

                String line = "";

                while (isRunning) {
                    try {
                        line = in.readUTF();
                        System.out.println(line);
                        if (line.equals("close")) {
                            System.out.println("Closing connection");
                            // close connection
                            try {
                                clientSocket.close();
                                in.close();
                                out.close();
                                isRunning = false;
                            } catch (IOException i) { System.out.println(i); }
                        } else if (line.equals("save")) {
                            receiveFile("text.txt");
                        }
                    }
                    catch(IOException i) { System.out.println("Error 2"); }
                    catch (Exception e) { throw new RuntimeException(e); }
                }
            }
            catch (IOException e) { e.printStackTrace(); }
            finally {
                try {
                    if (out != null) { out.close(); }
                    if (in != null) {
                        in.close();
                        clientSocket.close();
                    }
                } catch (IOException e) { e.printStackTrace(); }
            }
        }

        // receive file function is start here

        private void receiveFile(String fileName) throws Exception {
            int bytes = 0;
            FileOutputStream fileOutputStream = new FileOutputStream(fileName);

            long size = in.readLong(); // read file size
            byte[] buffer = new byte[4 * 1024];
            while (size > 0 && (bytes = in.read(buffer, 0, (int)Math.min(buffer.length, size))) != -1) {
                // Output text file content
                System.out.println(new String(buffer, StandardCharsets.UTF_8));
                // Here we write the file using write method
                fileOutputStream.write(buffer, 0, bytes);
                size -= bytes; // read upto file size
            }
            // Here we received file
            System.out.println("File is Received");
            fileOutputStream.close();
        }
    }
}
