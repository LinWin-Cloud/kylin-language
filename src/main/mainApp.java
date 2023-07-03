package main;

import KylinException.KylinRuntimeException;
import Program.KylinRuntime;

import javax.script.*;
import java.io.File;
import java.io.Reader;
import java.net.URISyntaxException;

public class mainApp {
    public static String MyHelpInformation = "Kylin Programming Language.\n" +
            "kylin [resource file]";
    public static ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
    public static ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("JavaScript");
    public static String jarDirectory;

    static {
        try {
            jarDirectory = new File(mainApp.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParent();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws Exception {
        int length = args.length;
        if (length == 0) {
            System.out.println(MyHelpInformation);
        }
        else {
            File target = new File(args[0]);
            if (target.exists() && target.isFile() && target.canRead()) {
                KylinRuntime main = new KylinRuntime("main");
                try {
                    //long s = System.currentTimeMillis();
                    main.code = baseFunction.getScript(target.getAbsolutePath());
                    main.run();
                    //long e = System.currentTimeMillis();
                    //System.out.println(e - s);
                }catch (Exception exception) {
                    KylinRuntimeException kylinRuntimeException = new KylinRuntimeException(exception.getMessage(), 0,true);
                    kylinRuntimeException.PrintErrorMessage(main);
                }
            }
            else {
                System.out.println("[ERR] Can not start resource file: "+args[1]);
                Runtime.getRuntime().exit(1);
            }
        }
    }
}
