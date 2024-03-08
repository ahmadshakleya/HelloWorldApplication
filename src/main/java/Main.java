import UDP.UDP_File_Client;
import UDP.UDP_File_Server;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        System.out.println("Main program running...");

        // Create and start the UDP server thread
        Thread serverThread = new Thread(() -> {
            UDP_File_Server.main(null);
        });
        serverThread.start();

        // Create and start the UDP client thread
        Thread clientThread = new Thread(() -> {
            UDP_File_Client.main(null);
        });
        clientThread.start();
    }
}
