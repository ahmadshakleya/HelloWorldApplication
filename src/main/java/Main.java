import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

// netstat -ant | grep 127
// https://www.geeksforgeeks.org/socket-programming-in-java/

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("System start");
        try {
            Socket socket = new Socket("127.0.0.1", 2680);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}