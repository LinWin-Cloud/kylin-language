

import KylinException.KylinRuntimeException;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ProxyService {
    private static final Init_ProxyService init_proxyService = new Init_ProxyService();
    private static int BootNum = 0;

    public static final ProxyService proxyService = new ProxyService();
    public static String ProxyUrl = "";
    public static int ProxyPort = 0;
    public static String version = "";

    public static void main(String[] args)
    {
        init_proxyService.init(args);

        try
        {

            if (proxyService.bootServerSocket())
            {
                try (ServerSocket serverSocket = new ServerSocket(ProxyService.ProxyPort)) {
                    for (int i = 0; i < 5; i++) {
                        Thread thread = new Thread(() -> {
                            while (true) {
                                try {
                                    Socket socket = serverSocket.accept();
                                    proxyService.ProxyServer(socket);
                                } catch (Exception exception) {
                                    KylinRuntimeException kylinRuntimeException = new KylinRuntimeException(exception.getMessage(), 0, true);
                                    kylinRuntimeException.setStackTrace(exception.getStackTrace());
                                }
                            }
                        });
                        thread.start();
                    }
                }
            }
        }catch (Exception exception){
            KylinRuntimeException kylinRuntimeException = new KylinRuntimeException(exception.getMessage(),0,true);
            kylinRuntimeException.setStackTrace(exception.getStackTrace());
        }
    }
    @SuppressWarnings("BusyWait")
    public Boolean bootServerSocket() throws InterruptedException {
        while (true)
        {
            try
            {
                ProxyService.BootNum = ProxyService.BootNum + 1;
                System.out.println("[Info] Start Port ["+ProxyService.BootNum+"]: "+ProxyService.ProxyPort);
                ServerSocket serverSocket = new ServerSocket(ProxyService.ProxyPort);
                serverSocket.close();
                break;
            }
            catch (Exception exception)
            {
                Thread.sleep(200);
                if (ProxyService.BootNum > 5)
                {
                    System.out.println("[ERR] Start Port Error.");
                    return false;
                }
                System.gc();
            }
        }
        return true;
    }
    public void ProxyServer(Socket socket)
    {
        try {
            OutputStream outputStream;
            InputStream inputStream;
            PrintWriter printWriter;
            BufferedReader bufferedReader;

            outputStream = socket.getOutputStream();
            inputStream = socket.getInputStream();

            printWriter = new PrintWriter(outputStream);
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String requests = bufferedReader.readLine();

            String getHttpMethod = requests.substring(0,requests.indexOf(" "));
            String getUrl = requests.substring(requests.indexOf(" ")+1,requests.lastIndexOf("HTTP/")-1);

            String ProxyUrl = ProxyService.ProxyUrl + getUrl;


            Requests requestsDom = new Requests();
            requestsDom.Setting(socket,printWriter,outputStream);
            requestsDom.setMethod(getHttpMethod);

            requestsDom.RequestsUrl(ProxyUrl);
        }
        catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}
