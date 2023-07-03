package Program;


import KylinException.KylinRuntimeException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class KylinProgramBaseFunction {
    private static String[] base = {
        "out",
        "for",
        "shell",
        "shell",
        "exception"
    };
    public static boolean runProgramBaseFunction(String code , KylinRuntime kylinRuntime) throws Exception {
        String function = code.substring(0 , code.indexOf("(")).trim();
        String input = code.substring(code.indexOf("(")+1 , code.lastIndexOf(")")).trim();
        String keyword = kylinRuntime.defined_func.get(function);
        if (keyword == null) {
            keyword = "";
        }
        if (function.equals("out") || keyword.equals("out")) {
            System.out.println(new KylinExpression().getExpression(input , kylinRuntime));
        }
        if (function.equals("for") || keyword.equals("for")) {
            String[] getIn = input.split(",(?![^(]*\\))");
            try {
                String func = getIn[0];
                int range = Integer.parseInt(getIn[1].trim());
                int length = getIn.length;
                if (length == 2) {
                    for (int i = 0 ;i < range ;i++) {
                        kylinRuntime.exec(func , kylinRuntime.codeLine);
                    }
                    return true;
                }
                else if (length == 3) {
                    boolean fast = getIn[2].equalsIgnoreCase("true");
                    if (fast) {
                        ExecutorService executorService = Executors.newFixedThreadPool(100);
                        Future<Integer> future = null;
                        for (int i = 0 ;i < range ;i++) {
                            future = executorService.submit(new Callable<Integer>() {
                                @Override
                                public Integer call() throws Exception {
                                    kylinRuntime.exec(func , kylinRuntime.codeLine);
                                    return 0;
                                }
                            });
                        }
                        executorService.shutdown();
                        return true;
                    }else {
                        for (int i = 0 ;i < range ;i++) {
                            kylinRuntime.exec(func , kylinRuntime.codeLine);
                        }
                        return true;
                    }
                }else {
                    System.out.println("Syntax Error.");
                    System.exit(1);
                    return true;
                }
            }catch (Exception exception) {
                KylinRuntimeException kylinRuntimeException = new KylinRuntimeException(exception.getMessage() , kylinRuntime.codeLine+1,true);
                kylinRuntimeException.PrintErrorMessage(kylinRuntime);
                System.exit(1);
                return false;
            }
        }
        if (function.equals("clear") || keyword.equals("clear")) {
            String os = System.getProperty("os.name");
            if (os.contains("Windows")) {
                Runtime.getRuntime().exec("cls");
            }
            else {
                Runtime.getRuntime().exec("clear");
            }
        }
        if (function.equals("shell") || keyword.equals("shell")) {
            String os = System.getProperty("os.name");
            Process process;
            if (os.contains("Windows")) {
                process = Runtime.getRuntime().exec(new KylinExpression().getExpression(input,kylinRuntime));
            }
            else {
                process = Runtime.getRuntime().exec(new KylinExpression().getExpression(input,kylinRuntime));
            }
            InputStream inputStream = process.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            while (true) {
                String line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }
                System.out.println(line);
            }
            bufferedReader.close();
            inputStream.close();
            process.destroy();
        }
        if (function.equals("exception") || keyword.equals("exception")) {
            String[] splitIn = input.split(",(?![^(]*\\))");
            String func = splitIn[0].trim();
            String err = splitIn[1].trim();
            try {
                new KylinExpression().getExpression(func , kylinRuntime);
            }catch (Exception exception) {
                new KylinExpression().getExpression(err , kylinRuntime);
            }
        }
        return true;
    }
    public static boolean isProgramBaseFunction(String code , KylinRuntime kylinRuntime) {
        try {
            String function = code.substring(0 , code.indexOf("(")).trim();
            String input = code.substring(code.indexOf("(")+1 , code.lastIndexOf(")")).trim();
            String keyword = kylinRuntime.defined_func.get(function);
            if (keyword == null) {
                keyword = "";
            }
            for (String i : base) {
                if (i.equals(function) || keyword.equals(i)) {
                    return true;
                }
            }
            return false;
        }catch (Exception exception) {
            return false;
        }
    }
    public static boolean isDefinedFunction(String code) {
        try {
            String function = code.substring(0 , code.indexOf("(")).trim();
            String input = code.substring(code.indexOf("(")+1 , code.lastIndexOf(")")).trim();
            return true;
        }catch (Exception exception) {
            //exception.printStackTrace();
            return false;
        }
    }
    public static boolean isRealDefinedFunction(String code , KylinRuntime kylinRuntime) {
        try {
            String function = code.substring(0 , code.indexOf("(")).trim();
            return (isDefinedFunction(code) && kylinRuntime.FunctionMap.containsKey(function)) || (isDefinedFunction(code) && kylinRuntime.isFunction && kylinRuntime.PublicRuntime.FunctionMap.containsKey(function));
        }catch (Exception exception) {
            return false;
        }
    }
}
