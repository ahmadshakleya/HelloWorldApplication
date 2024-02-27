// A Java program for a Server
import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class UDP_Server {
    public static void main(String args[]) throws IOException {
        boolean isRunning = true;

        // Step 1 : Create a socket to listen at port 2000
        DatagramSocket ds = new DatagramSocket(2000);
        byte[] receive = new byte[65535];

        DatagramPacket DpReceive = null;
        while (isRunning) {
            // Step 2 : create a DatgramPacket to receive the data.
            DpReceive = new DatagramPacket(receive, receive.length);

            // Step 3 : revieve the data in byte buffer.
            ds.receive(DpReceive);

            System.out.println("Client:-" + data(receive));

            // Exit the server if the client sends "bye"
            if (data(receive).toString().equals("close")) {
                System.out.println("Client sent bye.....EXITING");
                isRunning = !data(receive).toString().equals("close");
            }

            // Clear the buffer after every message.
            receive = new byte[65535];
        }
    }

    // A utility method to convert the byte array
    // data into a string representation.
    public static StringBuilder data(byte[] a) {
        if (a == null) return null;
        StringBuilder ret = new StringBuilder();
        int i = 0;
        while (a[i] != 0) {
            ret.append((char) a[i]);
            i++;
        }
        return ret;
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
