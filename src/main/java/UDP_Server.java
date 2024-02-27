// A Java program for a Server
import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class UDP_Server {
    private static DatagramSocket server = null;
    public static void main(String args[]) throws IOException {
        boolean isRunning = true;

        // Step 1 : Create a socket to listen at port 2000
        server = new DatagramSocket(2000);
        byte[] receive = new byte[65535];

        while (isRunning) {
            // Step 2 : create a DatgramPacket to receive the data.
            DatagramPacket receivedPacket = new DatagramPacket(receive, receive.length);

            // Step 3 : revieve the data in byte buffer.
            server.receive(receivedPacket);

            ClientHandler handler = new ClientHandler(receivedPacket);
            new Thread(handler).start();

            System.out.println("Client:-" + data(receive));

            // Exit the server if the client sends "close"
            if (data(receive).toString().equals("close")) {
                System.out.println("Client sent close.....EXITING");
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
    static class ClientHandler implements Runnable {
        private final DatagramPacket packet;

        public ClientHandler(DatagramPacket packet) { this.packet = packet; }

        @Override
        public void run() {
            try {
                try (FileOutputStream fileOutputStream = new FileOutputStream("text.txt")) {
                    while (true) {
                        server.receive(packet);
                        byte[] data = packet.getData();
                        fileOutputStream.write(data, 0, packet.getLength());

                        // Check for the end of file transmission
                        if (packet.getLength() < 1024) {
                            break;
                        }
                    }
                }
            } catch (Exception e) { e.printStackTrace(); }
        }
    }
}
