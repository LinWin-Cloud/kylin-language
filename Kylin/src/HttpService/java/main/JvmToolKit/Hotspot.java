package main.JvmToolKit;

import main.Main;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;

public class Hotspot {
    public void NetWork_Hotspot() {
        /**
         * In the first , jvm cannot build the local machine code.
         * So we must do something that the java vm build the local machine code.
         * After the local machine code is compiled , the java applications will run faster.
         * [Notice] this function only use by network.
         */
        try {
            String IP = Main.IP;
            if (IP.trim().equals("*")) {
                IP = "127.0.0.1";
            }
            for (int i = 0 ; i < 500 ; i++)
            {
                Socket socket = new Socket(IP , Main.port);
                OutputStream outputStream = socket.getOutputStream();
                PrintWriter printWriter = new PrintWriter(outputStream);
                printWriter.println("GET / HTTP/1.1");
                printWriter.println();
                printWriter.flush();
                printWriter.close();
                socket.close();
                outputStream.close();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
