package TCP;

import java.io.*;
import java.net.Socket;

public class TCP_File_Client {
    public static void main(String[] args) {

        try {
            // Open streams for communication
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

            // Get server IP address from user input
            System.out.print("Enter server IP address: ");
            String SERVER_IP = userInput.readLine();

            // Get server port from user input
            System.out.print("Enter server port: ");
            int PORT = Integer.parseInt(userInput.readLine());

            Socket socket = new Socket(SERVER_IP, PORT);

            userInput = new BufferedReader(new InputStreamReader(System.in));
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

            // Close connection
            socket.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
