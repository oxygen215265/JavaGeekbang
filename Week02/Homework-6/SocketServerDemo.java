import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketServerDemo {

    public static void main(String[] args) throws IOException {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        ServerSocket serverSocket = new ServerSocket(8001);
        while(true) {
            final Socket socket = serverSocket.accept();
            //executorService.submit(() -> service(socket));
            service(socket);
        }
    }

    public static void service(Socket socket) {
        try {
            Writer out;
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
            printWriter.println("HTTP/1.1 200 OK");
            printWriter.println("Content-Type: text/html; charset=utf-8");
            String response = "hello nio";
            printWriter.println("Content-Length:"+response.getBytes().length);
            printWriter.println();
            printWriter.write(response);
            printWriter.close();
            socket.close();
        } catch (IOException e) {

        }

    }
}
