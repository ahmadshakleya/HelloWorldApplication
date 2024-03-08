import java.net.*;
import java.io.*;

public class Server {
    // initialize socket and input stream
    private ServerSocket server = null;  // ServerSocket object for accepting client connections

    // constructor with port
    public Server(int port) {
        // starts server and waits for a connection
        try {
            server = new ServerSocket(port);  // Create a ServerSocket object to listen for client connections on the specified port
            System.out.println("Server started");  // Print message indicating that the server has started

            while (true) {
                System.out.println("Waiting for a client ...");  // Print message indicating that the server is waiting for a client to connect
                //new TCP.ClientHandler(server.accept()).start();  // Accept a new client connection and start a new TCP.ClientHandler thread to handle the client
            }
        } catch (IOException i) {  // Catch block to handle IOException
            System.out.println(i);  // Print error message if an I/O exception occurs
        }
    }

    public static void main (String args[])
    {
        Server server = new Server(5000);  // Create a new Server object and start it on port 5000
    }
}
/*
class TCP.ClientHandler extends Thread {
    private Socket socket = null; // Socket object for handling client connections
    private DataInputStream in = null; // DataInputStream for receiving data from the client

    private DataOutputStream out = null;

    // Constructor to initialize the socket and input stream
    public TCP.ClientHandler(Socket socket) {
        this.socket = socket; // Initialize the socket object
    }

    public void run() {
        try {
            System.out.println("Client accepted"); // Print message indicating that the client handler has started

            in = new DataInputStream(new BufferedInputStream(socket.getInputStream())); // Create a DataInputStream to receive data from the client

            // sends output to the socket
            out = new DataOutputStream(socket.getOutputStream());

            String line = ""; // Initialize a string to store received messages from the client

            //receiveFile(".\\src\\main\\resources\\test3.txt");
            //receiveFile(".\\src\\main\\resources\\test4.txt");

            sendFile(".\\src\\main\\resources\\send1.txt");

            // reads message from client until "Over" is sent
            while (!line.equals("Over")) {  // Loop until the received message is "Over"
                try {
                    line = in.readUTF();  // Read a UTF-8 encoded string from the client
                    System.out.println(line);  // Print the received message from the client
                } catch (IOException i) {  // Catch block to handle IOException
                    System.out.println(i);  // Print error message if an I/O exception occurs
                }
            }
            System.out.println("Closing connection");  // Print message indicating that the connection is being closed

            // close connection
            socket.close();  // Close the client socket
            in.close();  // Close the input stream
        } catch (IOException i) {  // Catch block to handle IOException
            System.out.println(i);  // Print error message if an I/O exception occurs
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void receiveFile(String fileName) throws Exception {
        int bytes = 0;
        FileOutputStream fileOutputStream = new FileOutputStream(fileName);

        long size = in.readLong(); // read file size
        byte[] buffer = new byte[4*1024];
        while (size > 0 && (bytes = in.read(buffer, 0, (int)Math.min(buffer.length, size))) != -1) {
            fileOutputStream.write(buffer,0,bytes);
            size -= bytes;
        }
        fileOutputStream.close();
    }

    private void sendFile(String path) throws Exception {
        int bytes = 0;
        File file = new File(path);
        FileInputStream fileInputStream = new FileInputStream(file);

        // Send file size
        out.writeLong(file.length());
        // Break file into chunks
        byte[] buffer = new byte[4*1024];
        while ((bytes=fileInputStream.read(buffer)) != -1) {
            out.write(buffer,0,bytes);
            out.flush();
        }
        fileInputStream.close();
    }
}

*/