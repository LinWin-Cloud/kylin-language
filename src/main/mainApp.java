package main;

import KylinException.KylinRuntimeException;
import Program.KyLinRuntime;
import Program.KyLinValue;

import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.*;

public class mainApp {
    public static PrintStream printStream = new PrintStream(System.out);
    public static String MyHelpInformation = "\n" +
            "Kylin Programming Language.\n" + "" +
            "   -version            Show the version information.\n" +
	        "   -r [kylin code]	    Run the kylin code.\n" +
            "   -console            Enter into the kylin console.\n" + "kylin [resource file]";
    public static String jarDirectory = null;
    public static Pattern pattern_split_expression = Pattern.compile(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)(?=([^\\(]*\\([^\\)]*\\))*[^\\)]*$)");
    public static HashMap<String , String> import_lib_value = new HashMap<>();
    public static HashMap<String , KyLinValue> all_kylin_value_pointer = new HashMap<>();
    public static ConcurrentHashMap<String , Thread> all_kylin_thread_map = new ConcurrentHashMap<>();
    public static double version = 4.3;
    public static String version_type = "Public";

    static {
        try {
            //获取jar包的位置
            jarDirectory = new File(mainApp.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParent();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
    public static File file;

    public static void main(String[] args) throws Exception {
        //System.out.println(jarDirectory);
        int length = args.length;
        if (length == 0) {
            System.out.println(MyHelpInformation);
        }
        else {
            PathLoader pathLoader = new PathLoader();
            pathLoader.LoadNewPath();
	        if(args[0].equals("-r")) {
                //System.out.println(args[0]);
                //System.out.println(args[1]);
	    	    try {
			        KyLinRuntime main = new KyLinRuntime("main");
                    main.exec(args[1] , 0);
		        }catch(Exception e) {
			        e.printStackTrace();
                    System.out.println("[ERR] Must input kylin code.");
		        }
                return;
	        }
            if (args[0].equals("-console")) {
                /**
                 * 参数是 -console , 进入调试控制台，不过这里不允许加入函数
                 */
                Scanner scanner = new Scanner(System.in);
                KyLinRuntime kyLinRuntime = new KyLinRuntime("main");
                kyLinRuntime.OnErrorExit = false;
                int i = 0;
                while (true) {
                    try {
                        System.out.print("Kylin> ");
                        String exec_code = scanner.nextLine();
                        i++;
                        ArrayList<String> a = new ArrayList<>();
                        a.add(exec_code);
                        kyLinRuntime.code = a;
                        kyLinRuntime.run();
                    }catch (Exception exception) {
                        continue;
                    }
                }
            }
            else if (args[0].equals("-version")) {
                System.out.println(version+" "+version_type);
                System.exit(0);
            }
            else {
                File target = new File(args[0]);
                file = target;
                if (target.exists() && target.isFile() && target.canRead()) {
                    //设置新的运行环境 main
                    KyLinRuntime main = new KyLinRuntime("main");
                    main.code_file = new File(target.getAbsolutePath());
                    try {
                        //long s = System.currentTimeMillis();
                        main.code = baseFunction.getScript(target.getAbsolutePath()); //把代码加载进入运行环境
                        main.run(); //运行代码
                        //long e = System.currentTimeMillis();
                        //System.out.println(e - s);
                    }

                    catch (Exception exception) {
                        (exception).printStackTrace();
                        KylinRuntimeException kylinRuntimeException = new KylinRuntimeException(exception.getMessage(), 0,false);
                        kylinRuntimeException.PrintErrorMessage(main);
                    }
                }
                else {
                    System.out.println("[ERR] Can not start resource file: "+args[0]);
                    Runtime.getRuntime().exit(1);
                }
            }
        }
    }
}
