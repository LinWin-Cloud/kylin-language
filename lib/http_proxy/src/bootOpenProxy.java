


import KylinException.KylinRuntimeException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class bootOpenProxy {
    public static void main(String[] args) {
        try {
            File file = new File("/usr/LinWinHttp/config/Multi-Proxy/");
            if (file.isDirectory()&&file.exists()) {
                //read all the json file of the multi open proxy config.
                File[] files = file.listFiles();
                for (int i = 0; i < Objects.requireNonNull(files).length; i++) {
                    //only read the json file.
                    //String fileLastName = files[i].getName().substring(files[i].getName().lastIndexOf("."), files.length);
                    String fileLastName = files[i].getName().substring(files[i].getName().lastIndexOf("."));
                    if (".json".equals(fileLastName)) {
                        //is json file. read it.
                        try {
                            Thread thread = getThread(i, files);
                            System.out.println("[!] Boot Server: "+thread.getName());

                        }catch (Exception exception){
                            KylinRuntimeException kylinRuntimeException = new KylinRuntimeException(exception.getMessage(),0,true);
                            kylinRuntimeException.setStackTrace(exception.getStackTrace());
                        }
                    }
                }
            } else {
                //target path is not a dir.
                System.out.println("Start error.");
            }
        } catch (Exception exception) {
            KylinRuntimeException kylinRuntimeException = new KylinRuntimeException(exception.getMessage(),0,true);
            kylinRuntimeException.setStackTrace(exception.getStackTrace());
        }
    }

    private static Thread getThread(int i, File[] files) {
        Thread thread = new Thread(() -> {
            String getServerPort = bootOpenProxy.readJson(files[i].getAbsolutePath(),"Server-Port");
            String getServerDir = bootOpenProxy.readJson(files[i].getAbsolutePath(),"ProxyUrl");
            //System.out.println(getServerDir+" "+getServerPort);
            try {
                //using system command to start the multi servlet.
                Process process = Runtime.getRuntime().exec("/usr/LinWinHttp/sys/BootMultiProxyVM.sh "+getServerDir+" "+getServerPort);
            } catch (IOException e) {
                KylinRuntimeException kylinRuntimeException = new KylinRuntimeException(e.getMessage(),0,true);
                kylinRuntimeException.setStackTrace(e.getStackTrace());
            }
        });
        thread.start();
        return thread;
    }

    public static String readJson(String filePath,String value) {
        //read and get the json files content.
        try {
            File file = new File(filePath);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            String jsonContent = "";

            List<String> list = new ArrayList<>();
            while ((line=bufferedReader.readLine())!=null) {
                list.add(line);
            }
            String[] listJSON = list.toArray(new String[0]);
            for (String s : listJSON) {
                String dealCode = bootOpenProxy.replaceSpace(s);
                if (dealCode != null) {
                    //System.out.println(dealCode);
                    String defaultValue = dealCode.substring(0, dealCode.indexOf("\""));
                    //System.out.println(defaultValue);
                    if (defaultValue.equals(value)) {
                        //return the config json files content and deal .
                        //boot the Http server.
                        String getContent = bootOpenProxy.getValueContent(s);
                        if (getContent != null) {
                            jsonContent = getContent;
                            break;
                        }
                    }
                }
            }
            return jsonContent;

        }catch (Exception exception) {
            KylinRuntimeException kylinRuntimeException = new KylinRuntimeException(exception.getMessage(),0,true);
            kylinRuntimeException.setStackTrace(exception.getStackTrace());
            return null;
        }
    }
    public static String replaceSpace(String code) {
        String space = "";
        for (int i=0; i < code.length();i++) {
            if (code.contains("\"")) {
                space = code.substring(code.indexOf("\"")+1);
                break;
            }
            else {
                space = null;
            }
        }
        return space;
    }
    public static String getValueContent(String code) {
        String content = "";
        for (int i = 0; i< code.length();i++) {
            if (code.contains(":")) {
                String tmpCode = code.substring(code.indexOf(":")+1);
                content = tmpCode.substring(tmpCode.indexOf("\"")+1,tmpCode.lastIndexOf("\""));
                break;
            }else {
                content = null;
            }
        }
        return content;
    }
}
