// A Java program for a Client
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class UDP_Client {
    public static void main(String args[]) throws Exception {
        boolean isRunning = true;
        Scanner sc = new Scanner(System.in);

        // Step 1:Create the socket object for
        // carrying the data.
        DatagramSocket ds = new DatagramSocket();

        InetAddress ip = InetAddress.getLocalHost();
        byte buf[] = null;

        // loop while user not enters "bye"
        while (isRunning) {
            String inp = sc.nextLine();

            // convert the String input into the byte array.
            buf = inp.getBytes();

            // Step 2 : Create the datagramPacket for sending
            // the data.
            DatagramPacket DpSend = new DatagramPacket(buf, buf.length, ip, 2000);

            // Step 3 : invoke the send call to actually send
            // the data.
            ds.send(DpSend);

            // break the loop if user enters "bye"
            isRunning = !inp.equals("close");

            if (inp.equals("save")) {
                String filepath = pickAFile();
                if (filepath != null) sendFile(ds, filepath, ip);
                else System.out.println("file not found");
            }
        }
    }

    private static String pickAFile() {
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

    // sendFile function define here
    private static void sendFile(DatagramSocket ds, String path, InetAddress ip) throws Exception {
        byte buf[] = new byte[1024];
        // Open the File where he located in your pc
        File file = new File(path);
        FileInputStream fileInputStream = new FileInputStream(file);

        // Step 3 : invoke the send call to actually send
        // the data.

        // Here we send the File to Server
        int i=0;
        while(fileInputStream.available()!=0){
            buf[i]=(byte)fileInputStream.read();
            i++;
        }
        // close the file here
        fileInputStream.close();

        DatagramPacket DpSend = new DatagramPacket(buf, buf.length, ip, 2000);
        ds.send(DpSend);
    }
}