package main;



import HttpService.HttpService;
import main.JvmToolKit.Hotspot;
import main.ToolKit.SimpleJson;
import HttpService.HTML.*;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static int boot_number = 0; 
    public static int port = 0;
    public static String IP;
    public static String HtmlPath;
    public static String ServerName = "LinwinSHS/";
    public static String ERROR_Page;
    public static main.ToolKit.SimpleJson Http_Service_Config = new SimpleJson();
    public static HttpService httpService = new HttpService();
    public static ExecutorService executorService = Executors.newFixedThreadPool(2000);
    public static HashSet<String> IndexFile = new HashSet<>();
    public static String Charset;
    public static VirtualContent virtualContent = new VirtualContent();
    public static boolean HttpServiceOK = false;
    public static String Access_Control_Allow_Origin;
    public static boolean Access_Control_Allow_Credentials;
    public static Hashtable<String,Boolean> Access_Control_Allow_Methods = new Hashtable<>();
    public static int ddos_requests;
    public static Hashtable<String,Integer> RequestsIP = new Hashtable<>();
    public static Hashtable<String,Integer> PHP_Requests = new Hashtable<>();
    public static int requests_php_number;
    public static String version;

    public static void LoadConfig()
    {
        Http_Service_Config.setFile("../config/http_service.json");
        port = Integer.parseInt(Http_Service_Config.get("port"));
        IP = Http_Service_Config.get("IP");

        String[] Index = Http_Service_Config.get("default").split(",");
        IndexFile.addAll(Arrays.asList(Index));

        String HTML_Path = Http_Service_Config.get("html");
        if (!new File(HTML_Path).isDirectory())
        {
            System.out.println("[ERR] CAN NOT FIND TARGET SERVICE DIR: "+HTML_Path);
            System.exit(1);
        }
        Main.HtmlPath = HTML_Path;
        Main.Charset = Http_Service_Config.get("charset");

        String error_page = Http_Service_Config.get("error_page");
        if (!new File(error_page).isDirectory())
        {
            System.out.println("[ERR] CAN NOT FIND TARGET ERROR PAGE DIR: "+HTML_Path);
            System.exit(1);
        }
        Main.ERROR_Page = error_page;
        virtualContent.load(new File(HTML_Path));
        virtualContent.reload_onTime();

        String URL = Http_Service_Config.get("Access-Control-Allow-Origin").trim();
        Main.Access_Control_Allow_Origin = URL;
        Main.Access_Control_Allow_Credentials
                = Http_Service_Config.get(
                        "Access-Control-Allow-Credentials").toLowerCase().trim().equals("true");

        String AllowMethod = Http_Service_Config.get("Access-Control-Allow-Methods");
        for (String i : AllowMethod.split(","))
        {
            Main.Access_Control_Allow_Methods.put(i.trim().toLowerCase(),true);
        }
        Main.requests_php_number = Integer.parseInt(Http_Service_Config.get("php_requests"));

        try {
            FileReader fileReader = new FileReader("../config/version.md");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            Main.version = bufferedReader.readLine();
            Main.ServerName = Main.ServerName + Main.version;
        }
        catch (Exception exception) {
            System.exit(1);
        }
    }
    public static void main(String[] args) {
        LoadConfig(); // load all the config file and project to the jvm

        /**
            Thread log = new Thread(new Runnable() {
                @Override
                public void run() {
                    logService.setPath("../logs/");
                    logService.setFileName("shs_log_");
                    logService.run();
                }
            });
            log.start();
         */
        /**
            Thread run_write_log = new Thread(new Runnable() {
                @Override
                public void run() {
                    logService.run_write();
                }
            });
            run_write_log.start();
         */

        Thread jvm_hotspot =
                new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        while (true) {
                            try
                            {
                                Thread.sleep(50);
                                if (HttpServiceOK)
                                {
                                    Hotspot hotspot = new Hotspot();
                                    //hotspot.NetWork_Hotspot();
                                    break;
                                }
                            }
                            catch (Exception exception)
                            {
                                exception.printStackTrace();
                            }
                        }
                    }
                });
        jvm_hotspot.start();

        httpService.run();
    }
}
