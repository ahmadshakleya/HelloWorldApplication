package TCP;

import java.io.*;
import java.net.Socket;

public class TCP_File_Client {
    public static void main(String[] args) {
        final String SERVER_IP = "127.0.0.1";
        final int PORT = 5000;

        try {
            Socket socket = new Socket(SERVER_IP, PORT);

            // Open streams for communication
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            System.out.println("Enter file name: ");
            String filename = userInput.readLine();

            // Send file name to server
            out.println(filename);

            // Receive file from server
            FileOutputStream fileOutputStream = new FileOutputStream("received_" + filename);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = socket.getInputStream().read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, bytesRead);
            }
            fileOutputStream.close();

            // Display response from server
            String response = in.readLine();
            System.out.println("Server response: " + response);

            // Close connection
            socket.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
