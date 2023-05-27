package HttpService;


import main.Main;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class WebServer {

    public static int Start_Test = 0;
    public String httpUrl = null;
    public String httpMethod = "GET";
    public Socket socket = null;
    public OutputStream outputStream = null;


    public void set(
            Socket socket,
            OutputStream outputStream,
            String HttpUrl,
            String HttpMethod) {
        this.socket = socket;
        this.outputStream = outputStream;
        this.httpUrl = HttpUrl;
        this.httpMethod = HttpMethod;
    }
    private void setServerSocket(ServerSocket serverSocket) throws Exception {
        if (Main.IP.trim().equals("*"))
        {
            serverSocket.bind(new InetSocketAddress(Main.port));
        }
        else {
            serverSocket.bind(
                    new InetSocketAddress(
                            Main.IP,
                            Main.port));
        }
        return;
    }
    public void mainWebServer() throws Exception
    {
        WebServer.getServerSocket(Main.port);
        ServerSocket serverSocket = new ServerSocket();
        this.setServerSocket(serverSocket);

        System.out.println(" [INFO] START WEB SERVER ["+Main.port+"]");
        Main.HttpServiceOK = true;

        while (true)
        {
            Socket socket = serverSocket.accept();
            Future<Integer> future = Main.executorService.submit(new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    runEXE(socket);
                    return 0;
                }
            });
        }
    }
    public static void runEXE(Socket socket) {
        try {
            /**
            Integer RequestsVisit = Main.RequestsIP.get(
                    socket.getInetAddress().toString());
            if (RequestsVisit == null) {
                Main.RequestsIP.put(
                        socket.getInetAddress().toString(),
                        1
                );
            }
            else {
                int i = RequestsVisit + 1;
                Main.RequestsIP.put(
                        socket.getInetAddress().toString() ,
                        i);
                if (i > Main.ddos_requests) {
                    socket.close();
                }
            }
            */

            OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(
                            new InputStreamReader(
                                    inputStream,
                                    StandardCharsets.UTF_8));
            PrintWriter printWriter = new PrintWriter(outputStream);
            ArrayList<String> HttpTitle = new ArrayList<>();
            while (true)
            {
                String line = bufferedReader.readLine();
                if (line == null)
                {
                    socket.close();
                    return;
                }
                if (line.equals("") || line.equals(" "))
                {
                    break;
                }
                HttpTitle.add(line);
            }
            //Main.logService.SaveLog(HttpTitle.toString());
            try
            {
                String httpUrl = HttpTitle.get(0);
                httpUrl = URLDecoder.decode(
                        httpUrl,
                        "UTF-8").trim();

                String httpMethod = httpUrl.substring(0,
                        httpUrl.indexOf(" ")).toLowerCase();

                httpUrl = httpUrl.substring(
                        httpUrl.indexOf(" ")+1,
                        httpUrl.lastIndexOf("HTTP/")-1);

                Boolean AllowMethod = Main.Access_Control_Allow_Methods.get(httpMethod);
                if (AllowMethod != null)
                {
                    WebServer.FutureEXE(
                            socket,
                            printWriter,
                            outputStream,
                            bufferedReader,
                            httpUrl);
                }
                else {
                    WebService webServiceServer = new WebService();
                    webServiceServer.sendFile(
                            Main.ERROR_Page+"/405.html",
                            405,printWriter,
                            socket,
                            outputStream);
                }
            }catch (Exception exception)
            {
                WebService webServiceServer = new WebService();
                webServiceServer.sendFile(
                        Main.ERROR_Page+"/400.html",
                        400,
                        printWriter,
                        socket,
                        outputStream);
            }
        }
        catch (Exception exception) {
        }
    }
    public static void getServerSocket(int port) throws InterruptedException
    {
        try
        {
            ServerSocket serverSocket = new ServerSocket(port);
            serverSocket.close();
        }
        catch (Exception exception)
        {
            System.out.println(" [ERR] Bind Port: "+Main.port);
            Thread.sleep(200);
            if (Main.port >= 10) {
                System.exit(1);
            }
            getServerSocket(port);
        }
    }
    public static void FutureEXE(
            Socket socket,
            PrintWriter printWriter,
            OutputStream outputStream,
            BufferedReader bufferedReader,
            String httpUrl)
            throws Exception {
        WebService webServiceServer = new WebService();
        webServiceServer.Service(socket,printWriter,outputStream,bufferedReader,httpUrl);
    }
}