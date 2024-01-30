package HttpService;


import HttpService.HTML.VirtualWebObject;
import main.Main;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.*;

class WebService {

    public void sendFile(
            String path,
            int code,
            PrintWriter printWriter,
            Socket socket,
            OutputStream outputStream) throws Exception {

        printWriter.println("HTTP/1.1 "+code+" OK");
        printWriter.println("Content-Type: "+new HttpFileContentType().getType(path));
        printWriter.println("Server: "+ Main.ServerName);
        printWriter.println("Length: "+new File(path).length());
        printWriter.println("Access-Control-Allow-Origin: "+Main.Access_Control_Allow_Origin);
        printWriter.println("Access-Control-Allow-Credentials: "+Main.Access_Control_Allow_Credentials);
        printWriter.println();
        printWriter.flush();

        FileInputStream fileInputStream = new FileInputStream(path);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);

        FileChannel channel = fileInputStream.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        int length = 0;
        while ((length = channel.read(buffer)) != -1)
        {
            byte[] bytes = buffer.array();
            buffer.clear();
            bufferedOutputStream.write(bytes,0,length);
            bufferedOutputStream.flush();
        }

        channel.close();
        outputStream.close();
        bufferedOutputStream.close();
        socket.close();
        printWriter.close();
        fileInputStream.close();
    }
    public void sendDirectory(
            String path,
            int code,
            PrintWriter printWriter,
            Socket socket,
            OutputStream outputStream) throws IOException {
        try
        {
            File file = new File(path);

            int last1 = path.length() - 1 ;
            int end = path.length();
            String charStr = path.substring(last1,end);
            if (!charStr.equals("/"))
            {
                printWriter.println("HTTP/1.1 "+200+" OK");
                printWriter.println("Content-Type: text/html ; Charset: "+Main.Charset);
                printWriter.println("Server: "+ Main.ServerName);
                printWriter.println("Length: "+new File(path).length());
                printWriter.println("Access-Control-Allow-Origin: "+Main.Access_Control_Allow_Origin);
                printWriter.println("Access-Control-Allow-Credentials: "+Main.Access_Control_Allow_Credentials);
                printWriter.println();
                printWriter.flush();
                printWriter.println("<script>window.location.href=window.location.href+'/';</script>");
            }

            for (String i : Main.IndexFile)
            {
                File file1 = new File(path+"/"+i);
                if (file1.exists() && file1.isFile())
                {
                    try{
                        sendFile(file1.getAbsolutePath(),200,printWriter,socket,outputStream);
                    }catch (Exception exception)
                    {
                        this.sendFile(Main.ERROR_Page+"/500.html",500,printWriter,socket,outputStream);
                    }
                    break;
                }
            }
            /**
             * If there do not have the Index file , then openLinwin will List all the files on this Dir
             */
            File[] ListFiles = file.listFiles();

            printWriter.println("HTTP/1.1 "+code+" OK");
            printWriter.println("Content-Type: text/html ; Charset: "+Main.Charset);
            printWriter.println("Server: "+Main.ServerName);
            printWriter.println("Length: "+new File(path).length());
            printWriter.println("Access-Control-Allow-Origin: "+Main.Access_Control_Allow_Origin);
            printWriter.println("Access-Control-Allow-Credentials: "+Main.Access_Control_Allow_Credentials);
            printWriter.println();
            printWriter.flush();

            printWriter.println("<h3>IndexOF: "
                    +path.substring(
                            path.indexOf(Main.HtmlPath)
                                    +Main.HtmlPath.length()) +"</h3>");
            printWriter.println("<a href='../'>Back</a><br /><br />");
            for (int i = 0 ; i < ListFiles.length ; i ++)
            {
                if (ListFiles[i].isDirectory()) {
                    printWriter.println(
                            "<a href='./"+ListFiles[i].getName()
                                    +"/'>* Directory -- "
                                    +ListFiles[i].getName()+"</a><br />"
                    );
                    printWriter.println();
                    printWriter.flush();
                }
            }
            for (int i = 0 ; i < ListFiles.length ; i ++)
            {
                if (ListFiles[i].isFile()) {
                    printWriter.println("<a href='./"
                            +ListFiles[i].getName()
                            + "'>* File -- "+ListFiles[i].getName()
                            + "</a><br />");
                    printWriter.println();
                    printWriter.flush();
                }
            }
            printWriter.println(
                    "<div style='width:90%;height:3px;background-color:black></div>'"
            );
            printWriter.flush();
            printWriter.flush();
            socket.close();

        }catch (Exception exception) {
            socket.close();
        }
    }
    public void Service(
            Socket socket,
            PrintWriter printWriter,
            OutputStream outputStream,
            BufferedReader bufferedReader,
            String httpUrl) throws Exception {
        try
        {
            String Http = httpUrl;

            if (Http == null) {
                socket.close();
            }
            String path = Main.HtmlPath + Http;
            path = path.replace("//", "/");

            File RequestsPath = new File(path);

            VirtualWebObject virtualWebObject = Main.virtualContent.get(httpUrl);

            if (virtualWebObject != null) {
                //System.out.println(httpUrl);
                printWriter.println("HTTP/1.1 "+200+" OK");
                printWriter.println(
                        "Content-Type: "
                                +virtualWebObject.getContent_type()
                                +" ; Charset: "+Main.Charset);
                printWriter.println("Server: "+ Main.ServerName);
                printWriter.println("Length: "+new File(path).length());
                printWriter.println("Access-Control-Allow-Origin: "+Main.Access_Control_Allow_Origin);
                printWriter.println("Access-Control-Allow-Credentials: "+Main.Access_Control_Allow_Credentials);
                printWriter.println();
                printWriter.flush();
                printWriter.println(virtualWebObject.getContent());
                printWriter.flush();
                socket.close();
                return;
            }
            if (path.endsWith(".php"))
            {
                Integer RequestsVisit = Main.PHP_Requests.get(
                        socket.getInetAddress().toString());
                if (RequestsVisit == null) {
                    Main.PHP_Requests.put(
                            socket.getInetAddress().toString(),
                            1
                    );
                }
                else {
                    int i = RequestsVisit + 1;
                    Main.PHP_Requests.put(
                            socket.getInetAddress().toString() ,
                            i);
                    if (i > Main.requests_php_number) {
                        socket.close();
                        return;
                    }
                }
                printWriter.println("HTTP/1.1 "+200+" OK");
                printWriter.println(
                        "Content-Type: "
                                + "text/html"
                                + " ; Charset: "+Main.Charset);
                printWriter.println("Server: "+ Main.ServerName);
                printWriter.println("Length: "+new File(path).length());
                printWriter.println("Access-Control-Allow-Origin: "+Main.Access_Control_Allow_Origin);
                printWriter.println("Access-Control-Allow-Credentials: "+Main.Access_Control_Allow_Credentials);
                printWriter.println();
                printWriter.flush();

                String cmd = "php " + RequestsPath.getAbsolutePath();
                Process process = Runtime.getRuntime().exec(cmd);

                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    printWriter.println(line);
                    printWriter.flush();
                }
                socket.close();
                printWriter.close();
                bufferedReader.close();
                process.destroy();
                return;
            }
            if (RequestsPath.equals("/")) {
                for (String i : Main.IndexFile)
                {
                    File file1 = new File(path+"/"+i);
                    if (file1.exists() && file1.isFile())
                    {
                        try{
                            sendFile(
                                    file1.getAbsolutePath(),
                                    200,
                                    printWriter,
                                    socket,
                                    outputStream);
                        }catch (Exception exception)
                        {
                            this.sendFile(
                                    Main.ERROR_Page +"/500.html",
                                    500,
                                    printWriter,
                                    socket,
                                    outputStream);
                        }
                        break;
                    }
                }
            }
            if (httpUrl.equals("/?heljadsjflsdkjdsjfldsklafjkldsa-3i245hoijhrtojerhktj3405jfgildjgd823409j8fcv0rt8ew09tj804923v85t0-398jt0-34f8905"))
            {
                System.exit(0);
            }
            if (RequestsPath.exists() && RequestsPath.isFile()) {
                sendFile(
                        path,
                        200,
                        printWriter,
                        socket,
                        outputStream);
            } else if (RequestsPath.exists() && RequestsPath.isDirectory()) {
                sendDirectory(
                        path,
                        200,
                        printWriter,
                        socket,
                        outputStream);
            } else {
                this.sendFile(
                        Main.ERROR_Page+"/404.html",
                        404,
                        printWriter,
                        socket,
                        outputStream);
            }
            bufferedReader.close();
        }
        catch (Exception exception){
        }
    }
}

