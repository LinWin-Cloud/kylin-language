package Program;


import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class KylinProgramBaseFunction {
    public static boolean isProgramBaseFunction(String code , KylinRuntime kylinRuntime) {
        try {
            String function = code.substring(0 , code.indexOf("(")).trim();
            String input = code.substring(code.indexOf("(")+1 , code.lastIndexOf(")")).trim();
            if (function.equals("out")) {
                System.out.println(new KylinExpression().getExpression(input , kylinRuntime));
            }
            if (function.equals("for")) {
                String[] getIn = input.split(",\\s*");
                try {
                    String func = getIn[0];
                    int range = Integer.parseInt(getIn[1].trim());
                    int length = getIn.length;
                    if (length == 2) {
                        for (int i = 0 ;i < range ;i++) {
                            kylinRuntime.exec(func , kylinRuntime.codeLine);
                        }
                    }
                    else if (length == 3) {
                        boolean fast = getIn[2].equalsIgnoreCase("true");
                        if (fast) {
                            ExecutorService executorService = Executors.newFixedThreadPool(30);
                            for (int i = 0 ;i < range ;i++) {
                                Future<Integer> future = executorService.submit(new Callable<Integer>() {
                                    @Override
                                    public Integer call() throws Exception {
                                        kylinRuntime.exec(func , kylinRuntime.codeLine);
                                        return 0;
                                    }
                                });
                            }
                            executorService.shutdown();
                        }else {
                            for (int i = 0 ;i < range ;i++) {
                                kylinRuntime.exec(func , kylinRuntime.codeLine);
                            }
                        }
                    }else {
                        throw new Exception("Syntax Err!");
                    }
                }catch (Exception exception) {
                    System.out.println("[ERR] "+exception.getMessage());
                    System.exit(1);
                }
            }
            return true;
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
            return isDefinedFunction(code) && kylinRuntime.FunctionMap.containsKey(function);
        }catch (Exception exception) {
            return false;
        }
    }
}
