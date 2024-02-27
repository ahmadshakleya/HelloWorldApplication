import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

// netstat -ant | grep 127

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("System start");
//        try {
//            Socket socket = new Socket("127.0.0.1", 2680);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        try {
//            TCP_Client client = new TCP_Client("127.0.0.2", 5000);
//        } catch (UnknownHostException e) { throw new RuntimeException(e); }
        try {
            UDP_Client client = new UDP_Client("127.0.0.1", 2000);
        } catch (Exception e) { throw new Exception(e); }
    }
}

// Checklist
// Setup TCP client & server (Done)
// Send files from client to server
// Apply multithreading on server side to allow multiple clients
// Modify application to use UDP instead of TCP

// Git commands:
// git checkout tomK
// git branch -a
// git push --set-upstream origin tomK