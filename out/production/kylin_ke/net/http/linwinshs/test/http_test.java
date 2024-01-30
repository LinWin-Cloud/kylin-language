import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class http_test {
    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(1145);
        while (true) {
            Socket socket = serverSocket.accept();
            OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();

            PrintWriter printWriter = new PrintWriter(outputStream);
            String url = new BufferedReader(new InputStreamReader(inputStream)).readLine();
            //System.out.println(url);
            printWriter.println("hello world");
            printWriter.flush();
            socket.close();
        }
    }
}
