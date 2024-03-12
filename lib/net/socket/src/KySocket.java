
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class KySocket {
    public static void main(String[] args) throws Exception {
        //Scanner scanner = new Scanner(System.in);
        Socket socket = new Socket("0.0.0.0" , 8080);
        OutputStream outputStream = socket.getOutputStream();
        InputStream inputStream = socket.getInputStream();
        //PrintWriter printWriter = new PrintWriter(outputStream);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        while (true) {
            String line = bufferedReader.readLine();
            if (line == null) {
                break;
            }
            System.out.println(line);
        }
        outputStream.close();
        bufferedReader.close();
        inputStream.close();
        //scanner.close();
        socket.close();
    }
}
