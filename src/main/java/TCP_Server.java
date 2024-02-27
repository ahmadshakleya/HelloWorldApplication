// A Java program for a Server
import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

// https://www.geeksforgeeks.org/socket-programming-in-java/
// https://www.geeksforgeeks.org/transfer-the-file-client-socket-to-server-socket-in-java/
// https://www.w3schools.com/java/java_files_read.asp
// https://stackoverflow.com/a/40255184

public class TCP_Server {
    //initialize socket and input stream
    private Socket           socket  = null;
    private ServerSocket     server  = null;
    private DataInputStream  in      =  null;
    private DataOutputStream out     =  null;

    // constructor with port
    public TCP_Server(int port) {
        // starts server and waits for a connection
        try {
            server = new ServerSocket(port);
            System.out.println("Server started on port " + port);

            System.out.println("Waiting for a client ...");

            socket = server.accept();
            System.out.println("Client accepted");

            // takes input from the client socket
            in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            out = new DataOutputStream(socket.getOutputStream());

            String line = "";

            // reads message from client until "close" is sent
            while (!line.equals("close")) {
                try {
                    line = in.readUTF();
                    System.out.println(line);
                    if (line.equals("save")) {
                        receiveFile("text.txt");
                    }
                }
                catch(IOException i) { System.out.println(i); }
                catch (Exception e) { throw new RuntimeException(e); }
            }
            System.out.println("Closing connection");

            // close connection
            socket.close();
            in.close();
            out.close();
        } catch(IOException i) { System.out.println(i); }
    }
    public static void main(String args[]) {
        TCP_Server server = new TCP_Server(5000);
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
