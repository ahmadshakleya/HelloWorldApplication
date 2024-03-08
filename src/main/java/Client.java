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

            //sendFile(".\\src\\main\\resources\\test1.txt");
            //sendFile(".\\src\\main\\resources\\test2.txt");

            receiveFile(".\\src\\main\\resources\\receive1.txt");
        } catch (UnknownHostException u) {  // Catch block to handle UnknownHostException
            System.out.println(u);  // Print error message if an unknown host exception occurs
            return;
        } catch (IOException i) {  // Catch block to handle IOException
            System.out.println(i);  // Print error message if an I/O exception occurs
            return;
        } catch (Exception i) {
            System.out.println(i);
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

    private void receiveFile(String fileName) throws Exception {
        int bytes = 0;
        FileOutputStream fileOutputStream = new FileOutputStream(fileName);

        long size = input.readLong(); // read file size
        byte[] buffer = new byte[4*1024];
        while (size > 0 && (bytes = input.read(buffer, 0, (int)Math.min(buffer.length, size))) != -1) {
            fileOutputStream.write(buffer,0,bytes);
            size -= bytes;
        }
        fileOutputStream.close();
    }

    public static void main(String args[])
    {
        Client client = new Client("127.0.0.1", 5000);  // Create a new Client object and connect to the server with IP address "127.0.0.1" and port 5000
    }

}

/*

import java.io.*;
import java.net.*;

public class Client {
    private Socket socket = null;
    private DataInputStream input = null;
    private DataOutputStream out = null;

    public Client(String address, int port) {
        try {
            socket = new Socket(address, port);
            System.out.println("Connected");

            input = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            new Thread(() -> {
                while (true) {
                    try {
                        String filename = input.readUTF();
                        if (!filename.isEmpty()) {
                            sendFile(filename);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        } catch (IOException i) {
            System.out.println(i);
        }
    }

    private void sendFile(String filename) {
        try {
            File file = new File(filename);
            FileInputStream fis = new FileInputStream(file);
            long length = file.length();
            byte[] buffer = new byte[4096];
            out.writeUTF(file.getName());
            out.flush();
            out.writeInt((int) length);
            out.flush();
            int count;
            while ((count = fis.read(buffer)) > 0) {
                out.write(buffer, 0, count);
            }
            out.flush();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        Client client = new Client("127.0.0.1", 5000);
    }
}*/