// A Java program for a Client
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.net.*;

public class TCP_Client {
    // initialize socket and input output streams
    private Socket socket = null;
    private DataInputStream input = null;
    private DataOutputStream out = null;

    // constructor to put ip address and port
    public TCP_Client(String address, int port) throws UnknownHostException {
        // establish a connection
        try {
            socket = new Socket(address, port);
            System.out.println("Connected");

            // takes input from terminal
            input = new DataInputStream(System.in);

            // sends output to the socket
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException i) {
            System.out.println(i);
            return;
        }

        // string to read message from input
        String line = "";

        // keep reading until "Over" is input
        while (!line.equals("close")) {
            try {
                line = input.readLine();
                out.writeUTF(line);
                if (line.equals("save")) {
                    String filepath = pickAFile();
                    if (filepath != null) sendFile(filepath);
                    else System.out.println("file not found");
                }
            }
            catch (IOException i) { System.out.println(i); }
            catch (Exception e) { throw new RuntimeException(e); }
        }

        // close the connection
        try {
            input.close();
            out.close();
            socket.close();
        } catch (IOException i) { System.out.println(i); }
    }

    public static void main(String args[]) throws UnknownHostException {
        try {
            TCP_Client client = new TCP_Client("127.0.0.1", 5000);
        } catch (UnknownHostException e) { throw new RuntimeException(e); }
    }

    // sendFile function define here
    private void sendFile(String path) throws Exception {
        int bytes = 0;
        // Open the File where he located in your pc
        File file = new File(path);
        FileInputStream fileInputStream
                = new FileInputStream(file);

        // Here we send the File to Server
        out.writeLong(file.length());
        // Here we  break file into chunks
        byte[] buffer = new byte[4 * 1024];
        while ((bytes = fileInputStream.read(buffer))
                != -1) {
            // Send the file to Server Socket
            out.write(buffer, 0, bytes);
            out.flush();
        }
        // close the file here
        fileInputStream.close();
    }

    private String pickAFile() {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text files", "txt");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            //System.out.println("You chose to open this file: " + chooser.getSelectedFile().getName());
            //System.out.println("With the path: " + chooser.getSelectedFile().getAbsolutePath());
            return chooser.getSelectedFile().getAbsolutePath();
        }
        return null;
    }
}
