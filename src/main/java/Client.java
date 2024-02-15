// A Java program for a Client
import java.io.*;
import java.net.*;

public class Client {
    // initialize socket and input output streams
    private Socket socket = null;  // Socket object for establishing connection
    private DataInputStream input = null;  // Input stream to receive data from the server
    private DataOutputStream out = null;  // Output stream to send data to the server

    // constructor to put ip address and port
    public Client(String address, int port) {
        // establish a connection
        try {
            socket = new Socket(address, port);  // Create a new socket with the given IP address and port
            System.out.println("Connected");  // Print message indicating successful connection

            // takes input from terminal
            input = new DataInputStream(System.in);  // Create a DataInputStream to receive input from the terminal

            // sends output to the socket
            out = new DataOutputStream(socket.getOutputStream());  // Create a DataOutputStream to send data to the server
        } catch (UnknownHostException u) {  // Catch block to handle UnknownHostException
            System.out.println(u);  // Print error message if an unknown host exception occurs
            return;
        } catch (IOException i) {  // Catch block to handle IOException
            System.out.println(i);  // Print error message if an I/O exception occurs
            return;
        }

        // string to read message from input
        String line = "";  // Initialize a string to store input messages

        // keep reading until "Over" is input
        while(!line.equals("Over")) {  // Loop until the input message is "Over"
            try {
                line = input.readLine();  // Read a line of input from the terminal
                out.writeUTF(line);  // Send the input message to the server
            } catch (IOException i) {  // Catch block to handle IOException
                System.out.println(i);  // Print error message if an I/O exception occurs
            }
        }

        // close the connection
        try {
            input.close();  // Close the input stream
            out.close();  // Close the output stream
            socket.close();  // Close the socket
        } catch (IOException i) {  // Catch block to handle IOException
            System.out.println(i);  // Print error message if an I/O exception occurs
        }
    }

    public static void main(String args[])
    {
        Client client = new Client("127.0.0.1", 5000);  // Create a new Client object and connect to the server with IP address "127.0.0.1" and port 5000
    }

}
