package main;

import KylinException.KylinRuntimeException;
import Program.KylinRuntime;

import javax.script.*;
import java.io.File;
import java.net.URISyntaxException;

public class mainApp {
    public static String MyHelpInformation = "Kylin Programming Language.\n" +
            "kylin [resource file]";
    public static ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
    public static ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("JavaScript");  // JavaScript引擎，主要用于字符串拼接和运算，不过Kylin内置的也有支持
    public static String jarDirectory;

    static {
        try {
            //获取jar包的位置
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
                //设置新的运行环境 main
                KylinRuntime main = new KylinRuntime("main");
                try {
                    //long s = System.currentTimeMillis();
                    main.code = baseFunction.getScript(target.getAbsolutePath()); //把代码加载进入运行环境
                    main.run(); //运行代码
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
