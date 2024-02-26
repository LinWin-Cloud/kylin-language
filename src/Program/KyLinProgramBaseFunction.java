package Program;


import Function.RobotOptions;
import Function.Write;
import KylinException.KylinRuntimeException;
import main.baseFunction;
import main.mainApp;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static main.mainApp.printStream;

public class KyLinProgramBaseFunction {
    private static String[] base = {
        "out",
        "print",
        "for",
        "shell",
        "clear",
        "exception",
        "while",
        "write",
        "throw_error",
        "exit",
        "except",
        "list_add",
        "list_rm",
        "kill_thread",
        "gc",
        "enterKey",
        "del"
    };
    public static boolean runProgramBaseFunction(String code , KyLinRuntime kylinRuntime) throws Exception {
        String function = code.substring(0 , code.indexOf("(")).trim();
        String input = code.substring(code.indexOf("(")+1 , code.lastIndexOf(")")).trim();
        String keyword = "";
        //String keyword = kylinRuntime.defined_func.get(function);
        //if (keyword == null) {
        //    keyword = "";
        //}
        if (function.equals("out") || function.equals("print")) {
            printStream.println(new KyLinExpression().getExpression(input , kylinRuntime));
            printStream.flush();
            return true;
        }
        if (function.equals("del")) {
            String pointer = new KyLinExpression().getExpression(input , kylinRuntime);
            KyLinValue value = mainApp.all_kylin_value_pointer.get(pointer);
            value = null;
            //System.out.println(mainApp.all_kylin_value_pointer);
            mainApp.all_kylin_value_pointer.remove(pointer);
            return true;
        }
        if (function.equals("while")) {
            String[] getIn = input.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)(?=([^\\(]*\\([^\\)]*\\))*[^\\)]*$)");
            try {
                String con = getIn[0].trim();
                String func = getIn[1].trim();

                while (new KyLinBoolean().isBool(con,kylinRuntime)) {
                    kylinRuntime.exec(func , kylinRuntime.codeLine);
                }
            }catch (Exception exception) {
                KylinRuntimeException kylinRuntimeException = new KylinRuntimeException(exception.getMessage() , kylinRuntime.codeLine+1,true);
                kylinRuntimeException.PrintErrorMessage(kylinRuntime);
                System.exit(1);
                return false;
            }
        }
        if (function.equals("enterKey")) {
            String key = new KyLinExpression().getExpression(input , kylinRuntime);
            RobotOptions.enterKey(key);
        }
        if (function.equals("kill_thread")) {
            String pointer = new KyLinExpression().getExpression(input , kylinRuntime);
            mainApp.all_kylin_thread_map.get(pointer).interrupt();
            mainApp.all_kylin_thread_map.remove(pointer);
        }
        if (function.equals("list_rm")) {
            String[] getIn = input.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)(?=([^\\(]*\\([^\\)]*\\))*[^\\)]*$)");
            String list_object = getIn[0].trim();
            int rm_object = Integer.parseInt(getIn[1].trim());
            String address = KyLinUseFunction.getAddress(list_object , kylinRuntime).toString();
            KyLinValue k = mainApp.all_kylin_value_pointer.get(address);
            if (k.getType().equals("list")) {
                KyLinList kyLinList = (KyLinList) k.getContent();
                kyLinList.arrayList.remove(rm_object);
                k.setContent(kyLinList , kylinRuntime);
                mainApp.all_kylin_value_pointer.put(address , k);
            }else {
                throw new RuntimeException("TypeError");
            }
        }
        if (function.equals("list_add")) {
            String[] getIn = input.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)(?=([^\\(]*\\([^\\)]*\\))*[^\\)]*$)");
            String list_object = getIn[0].trim();
            String add_object = getIn[1].trim();

            //System.out.println(list_object+";"+add_object+";");
            String address = KyLinUseFunction.getAddress(list_object , kylinRuntime).toString();
            KyLinValue k = mainApp.all_kylin_value_pointer.get(address);
            if (k.getType().equals("list")) {
                KyLinList kyLinList = (KyLinList) k.getContent();
                KyLinValue n_var = new KyLinValue();
                n_var.setName(String.valueOf(baseFunction.getRandomLong()));
                n_var.setContent(new KyLinExpression().getExpression(add_object ,kylinRuntime) , kylinRuntime);
                n_var.setIs_public(true);
                n_var.setType(KyLinType.getType(add_object , kylinRuntime));
                kyLinList.arrayList.add(n_var);
                k.setContent(kyLinList , kylinRuntime);
                k.setType("list");
                mainApp.all_kylin_value_pointer.put(address , k);
            }else {
                throw new RuntimeException("TypeError");
            }
        }
        if (function.equals("gc")) {
            System.gc();
        }
        if (function.equals("for")) {
            String[] getIn = input.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)(?=([^\\(]*\\([^\\)]*\\))*[^\\)]*$)");
            try {
                String func = getIn[0];
                int range = Integer.parseInt(getIn[1].trim());
                int length = getIn.length;
                if (length == 2) {
                    try {
                        for (int i = 0 ; i < range ; i++) {
                            kylinRuntime.exec(func , 0);
                        }
                    }
                    catch (Exception exception) {
                        throw new Exception(exception);
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
                                    new KyLinExpression().getExpression(func,kylinRuntime);
                                    //kylinRuntime.exec(func, kylinRuntime.codeLine);
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
                    throw new Exception("Syntax error");
                }
            }catch (Exception exception) {
                KylinRuntimeException kylinRuntimeException = new KylinRuntimeException(exception.getMessage() , kylinRuntime.codeLine+1,true);
                kylinRuntimeException.PrintErrorMessage(kylinRuntime);
                System.exit(1);
                return false;
            }
        }
        if (function.equals("clear")) {
            String os = System.getProperty("os.name");
            if (os.contains("Windows")) {
                Runtime.getRuntime().exec("cls");
            }
            else {
                Runtime.getRuntime().exec("/bin/sh -c clear");
            }
        }
        if (function.equals("shell")) {
            String os = System.getProperty("os.name");
            // 执行Shell命令
            Process process = Runtime.getRuntime().exec(new KyLinExpression().getExpression(input , kylinRuntime));
            // 获取命令输出流
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
            // 获取命令错误流
            BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            // 读取输出
            String outputLine;
            while ((outputLine = stdInput.readLine()) != null) {
                System.out.println(outputLine);
            }
            // 读取错误输出
            while ((outputLine = stdError.readLine()) != null) {
                System.out.println("Error: " + outputLine);
            }

        }
        if (function.equals("exception") || function.equals("except")) {
            String[] splitIn = input.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)(?=([^\\(]*\\([^\\)]*\\))*[^\\)]*$)");
            String func = splitIn[0].trim();
            String err = splitIn[1].trim();
            //System.out.println(func+" "+err);
            try {
                new KyLinExpression().getExpression(func , kylinRuntime);
            }catch (Exception exception) {
                //exception.printStackTrace();
                try {
                    String name = err.substring(0,err.indexOf("("));
                    KyLinFunction kylinFunction = kylinRuntime.ExceptionMap.get(name);
                    if (kylinFunction == null) {
                        throw new Exception("Cannot find target exception function: "+ name);
                    }else {
                        KyLinValue kylinValue = new KyLinValue();
                        kylinValue.setName(kylinFunction.err_code);
                        kylinValue.setContent(exception.getMessage() , kylinRuntime);
                        kylinFunction.kylinRuntime.ValueMap.put(kylinFunction.err_code, kylinValue);

                        kylinFunction.kylinRuntime.run();
                    }
                }catch (Exception exception1) {
                    //exception1.printStackTrace();
                    throw new Exception(exception1);
                }
            }
        }
        if (function.equals("write")) {
            String[] splitIn = input.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)(?=([^\\(]*\\([^\\)]*\\))*[^\\)]*$)");
            String path = new KyLinExpression().getExpression(splitIn[0], kylinRuntime);
            String content = new KyLinExpression().getExpression(splitIn[1] , kylinRuntime);
            boolean isCover = new KyLinBoolean().isBool(splitIn[2],kylinRuntime);
            new Write().write(path,content,isCover);
        }
        if (function.equals("throw_error")) {
            throw new Exception(new KyLinExpression().getExpression(input,kylinRuntime));
        }
        if (function.equals("exit")) {
            if (input.equals("")) {
                System.exit(0);
            }
            System.exit(Integer.parseInt(new KyLinExpression().getExpression(input,kylinRuntime)));
        }
        /**
         * 当 function 的名字是使用函数的时候调用 KyLinUseFunction .
         */
        if (Arrays.asList(KyLinUseFunction.KylinKeyWord).contains(function)) {
            //System.out.println(1);
            //int index = Arrays.asList(KyLinUseFunction.KylinKeyWord).indexOf(function);
            KyLinUseFunction.UseFunction(code , kylinRuntime);
        }
        return true;
    }
    public static boolean isProgramBaseFunction(String code , KyLinRuntime kylinRuntime) {
        try {
            String function = code.substring(0 , code.indexOf("(")).trim();
            String input = code.substring(code.indexOf("(")+1 , code.lastIndexOf(")")).trim();
            for (String i : base) {
                if (i.equals(function)) {
                    return true;
                }
            }
            return Arrays.asList(KyLinUseFunction.KylinKeyWord).contains(function);
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
    public static boolean isRealDefinedFunction(String code , KyLinRuntime kylinRuntime) {
        try {
            String function = code.substring(0 , code.indexOf("(")).trim();
            return (isDefinedFunction(code) &&
                    kylinRuntime.FunctionMap.containsKey(function)) ||
                    (isDefinedFunction(code) &&
                            kylinRuntime.isFunction &&
                            kylinRuntime.PublicRuntime.FunctionMap.containsKey(function)) &&
                    Arrays.asList(KyLinUseFunction.KylinKeyWord).contains(function);
        }catch (Exception exception) {
            return false;
        }
    }
}
