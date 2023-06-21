package main;

import Program.KylinRuntime;

import java.io.File;

public class mainApp {
    public static String MyHelpInformation = "Kylin Programming Language.\n" +
            "kylin [resource file]";

    public static void main(String[] args) {
        int length = args.length;
        if (length == 0) {
            System.out.println(MyHelpInformation);
        }
        else {
            File target = new File(args[0]);
            if (target.exists() && target.isFile() && target.canRead()) {
                try {
                    //long s = System.currentTimeMillis();
                    KylinRuntime main = new KylinRuntime();
                    main.code = baseFunction.getScript(target.getAbsolutePath());
                    main.run();
                    //long e = System.currentTimeMillis();
                    //System.out.println(e - s);
                }catch (Exception exception) {
                    System.out.println(exception.getMessage());
                }
            }
            else {
                System.out.println("[ERR] Can not start resource file: "+args[1]);
                Runtime.getRuntime().exit(1);
            }
        }
    }
}
