package main;

import KylinException.KylinRuntimeException;
import Program.KyLinFunction;
import Program.KyLinRuntime;
import Program.KyLinValue;

import java.io.*;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.*;

public class mainApp {
    public static final PrintStream printStream = new PrintStream(System.out);
    public static final String MyHelpInformation = "\n" +
            "Kylin Programming Language.\n" +
            "   -version            Show the version information.\n" +
	        "   -r [kylin code]	    Run the kylin code.\n" +
            "   -console            Enter into the kylin console.\n" + "kylin [resource file]";
    public static final String jarDirectory;
    public static final Pattern pattern_split_expression = Pattern.compile(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)(?=([^\\(]*\\([^\\)]*\\))*[^\\)]*$)");
    public static final HashMap<String , String> import_lib_value = new HashMap<>();
    public static final HashMap<String , KyLinValue> all_kylin_value_pointer = new HashMap<>();
    public static final ConcurrentHashMap<String , Thread> all_kylin_thread_map = new ConcurrentHashMap<>();
    public static final double version = 4.5;
    public static final HashMap<String , KyLinFunction> all_kylin_function_pointer = new HashMap<>();
    public static final String version_type = "Beta";

    static {
        try {
            //获取jar包的位置
            jarDirectory = new File(mainApp.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParent();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
    public static File file;

    public static void main(String[] args) {
        int length = args.length;
        if (length == 0) {
            printHelpInformation();
        } else {
            PathLoader pathLoader = new PathLoader();
            pathLoader.LoadNewPath();
            switch (args[0]) {
                case "-r":
                    runKylinCode(args);
                    break;
                case "-console":
                    enterConsole();
                    break;
                case "-version":
                    printVersion();
                    break;
                default:
                    startResourceFile(args[0]);
                    break;
            }
        }
    }

    private static void printHelpInformation() {
        System.out.println(MyHelpInformation);
    }

    private static void runKylinCode(String[] args) {
        try {
            KyLinRuntime main = new KyLinRuntime("main");
            main.exec(args[1], 0);
        } catch (Exception e) {
            KylinRuntimeException kylinRuntimeException = new KylinRuntimeException(e.getMessage(), 0, true);
            kylinRuntimeException.setStackTrace(e.getStackTrace());
            System.out.println("[ERR] Must input kylin code.");
        }
    }

    private static void enterConsole() {
        Scanner scanner = new Scanner(System.in);
        KyLinRuntime kyLinRuntime = new KyLinRuntime("main");
        kyLinRuntime.OnErrorExit = false;
        int i = 0;
        while (true) {
            try {
                System.out.print("Kylin> ");
                String exec_code = scanner.nextLine();
                if ("exit".equals(exec_code)) {
                    break;
                }
                i++;
                ArrayList<String> a = new ArrayList<>();
                a.add(exec_code);
                kyLinRuntime.code = a;
                kyLinRuntime.run();
            } catch (Exception exception) {
                KylinRuntimeException kylinRuntimeException = new KylinRuntimeException(exception.getMessage(), i, false);
                kylinRuntimeException.setStackTrace(exception.getStackTrace());
            }
        }
    }

    private static void printVersion() {
        System.out.println(version + " " + version_type);
    }

    private static void startResourceFile(String filePath) {
        File target = new File(filePath);
        file = target;
        if (target.exists() && target.isFile() && target.canRead()) {
            KyLinRuntime main = new KyLinRuntime("main");
            main.code_file = new File(target.getAbsolutePath());
            try {
                main.code = baseFunction.getScript(target.getAbsolutePath());
                main.run();
            } catch (Exception exception) {
                KylinRuntimeException kylinRuntimeException = new KylinRuntimeException(exception.getMessage(), 0, false);
                kylinRuntimeException.PrintErrorMessage(main);
            }
        } else {
            System.out.println("[ERR] Can not start resource file: " + filePath);
            Runtime.getRuntime().exit(1);
        }
    }

}
