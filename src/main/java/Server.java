// A Java program for a Server
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
                new ClientHandler(server.accept()).start();  // Accept a new client connection and start a new ClientHandler thread to handle the client
            }
        } catch (IOException i) {  // Catch block to handle IOException
            System.out.println(i);  // Print error message if an I/O exception occurs
        }

    public static void main(String args[])
    {
        Server server = new Server(5000);  // Create a new Server object and start it on port 5000
    }
}

Class ClientHandler extends Thread {
    private Socket socket = null; // Socket object for handling client connections
    private DataInputStream in = null; // DataInputStream for receiving data from the client

    // Constructor to initialize the socket and input stream
    public ClientHandler(Socket socket) {
        this.socket = socket; // Initialize the socket object
    }

    public void run() {
        try {
            System.out.println("Client accepted"); // Print message indicating that the client handler has started

            in = new DataInputStream(new BufferedInputStream(socket.getInputStream())); // Create a DataInputStream to receive data from the client

            String line = ""; // Initialize a string to store received messages from the client

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
        }
        }
    }
}
