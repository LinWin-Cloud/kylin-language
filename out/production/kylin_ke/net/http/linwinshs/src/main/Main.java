package main;



import HttpService.HttpService;
import main.JvmToolKit.Hotspot;
import main.SimpleJson;
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
    public static String ServerName = "Linwinshs/";
    public static String ERROR_Page;
    public static main.SimpleJson Http_Service_Config = new SimpleJson();
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

    public static void LoadConfig(String[] args) throws Exception
    {
        port = Integer.parseInt(args[0]);
        IP = args[1];

        String[] Index = {"index.html" , "index.htm"};
        IndexFile.addAll(Arrays.asList(Index));

        String HTML_Path = args[2];
        if (!new File(HTML_Path).isDirectory())
        {
            System.out.println("[ERR] CAN NOT FIND TARGET SERVICE DIR: "+HTML_Path);
            throw new Exception("err");
        }
        Main.HtmlPath = HTML_Path;
        Main.Charset = "UTF-8";

        String jarDirectory = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParent();
        String error_page =  jarDirectory+"/error/";
        if (!new File(error_page).isDirectory())
        {
            System.out.println("[ERR] CAN NOT FIND TARGET ERROR PAGE DIR: "+error_page);
            throw new RuntimeException("err");
        }
        Main.ERROR_Page = error_page;

        String URL = "*";
        Main.Access_Control_Allow_Origin = URL;
        Main.Access_Control_Allow_Credentials = true;

        String AllowMethod = "GET,POST,HEAD,PUT,DELETE,CONNECT,OPTIONS,TRACE,PATCH";
        for (String i : AllowMethod.split(","))
        {
            Main.Access_Control_Allow_Methods.put(i.trim().toLowerCase(),true);
        }
        Main.requests_php_number = 1000;

        Main.version = "2.9.2";
        Main.ServerName = Main.ServerName + Main.version;
    }
    public static void main(String[] args) {
        try {
            LoadConfig(args); // load all the config file and project to the jvm

            httpService.run();
        }catch (Exception exception) {
            System.out.println("err");
        }
    }
}
