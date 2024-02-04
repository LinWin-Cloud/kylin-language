package Program;

import Function.TypeOf;
import KylinException.KylinRuntimeException;
import api.KyLin_GetFileContent;
import main.baseFunction;
import main.mainApp;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class KyLinUseFunction {
    public static String[] KylinKeyWord ={
            "getTime",                  // 0
            "input",                    // 1
            "get_os",                   // 2
            "get_path",                 // 3
            "long_time",                // 4
            "bool",                     // 5
            "typeof",                   // 6
            "file_exists",              // 7
            "get_file_content",         // 8
            "java_runtime",             // 9
            "length",                   // 10
            "pow",                      // 11
            "sub",                      // 12
            "index",                    // 13
            "lastindex",                // 14
            "delete",                   // 15
            "get_pointer",              // 16
            "toVal",                    // 17
            "shell_output",             // 18
            "new_thread",               // 19
            "get_mouse_point",          // 20
            "toInt",                    // 21
            "rm",                       // 22
    };
    public static boolean isUseFunction(String expression , KyLinRuntime kylinRuntime) {
        try {
            /**
             * [注释补充]
             * 判断是否是可调用的 函数
             *
             */
            String funcName = expression.substring(0,expression.indexOf("(")).trim();
            boolean is = false;
            for (String s : KylinKeyWord) {
                if (s.equals(funcName))
                {
                    is = true;
                    break;
                }
            }
            return is;
        }catch (Exception exception) {
            return false;
        }
    }
    public static KyLinValue UseFunction(String expression , KyLinRuntime kylinRuntime) throws Exception {
        /**
         * [注释补充]
         * 调用的 函数
         * 就是调用这些函数，这些都是内置的.
         */
        String funcName = expression.substring(0,expression.indexOf("(")).trim();
        String content = expression.substring(expression.indexOf("(")+1 , expression.lastIndexOf(")")).trim();
        String[] split = content.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)(?=([^\\(]*\\([^\\)]*\\))*[^\\)]*$)");

        if (funcName.equals("getTime")) {
            KyLinValue value = new KyLinValue();
            value.setType("string");
            value.setContent(baseFunction.getTime() , kylinRuntime);
            value.setIs_public(true);

            return value;
        }else if (funcName.equals("input")) {
            KyLinValue value = new KyLinValue();
            value.setType("string");
            value.setContent(baseFunction.input(new KyLinExpression().getExpression(split[0] , kylinRuntime)) , kylinRuntime);
            value.setIs_public(true);

            return value;
        }
        else if (funcName.equals("get_os")) {
            KyLinValue value = new KyLinValue();
            value.setType("string");
            value.setContent(System.getProperty("os.name") , kylinRuntime);
            value.setIs_public(true);
            return value;
        }
        else if (funcName.equals("get_path")) {
            KyLinValue value = new KyLinValue();
            value.setType("string");
            value.setContent(System.getProperty("user.dir") , kylinRuntime);
            value.setIs_public(true);
            return value;
        }
        else if (funcName.equals("long_time")) {
            KyLinValue value = new KyLinValue();
            value.setType("num");
            value.setContent(System.currentTimeMillis() , kylinRuntime);
            value.setIs_public(true);
            return value;
        }
        else if (funcName.equals("bool")) {
            KyLinValue value = new KyLinValue();
            value.setType("bool");
            value.setContent(new KyLinBoolean().isBool(content,kylinRuntime) , kylinRuntime);
            value.setIs_public(true);
            return value;
        }
        else if (funcName.equals("typeof")) {
            KyLinValue value = new KyLinValue();
            value.setType("string");
            value.setContent(KyLinType.getType(content , kylinRuntime), kylinRuntime);
            value.setIs_public(true);
            return value;
        }
        else if (funcName.equals("file_exists")) {
            KyLinValue value = new KyLinValue();
            value.setType("bool");
            value.setContent(new File(new KyLinExpression().getExpression(content , kylinRuntime)).exists(), kylinRuntime);
            value.setIs_public(true);
            return value;
        }
        else if (funcName.equals("get_file_content")) {
            KyLinValue value = new KyLinValue();
            value.setType("string");
            value.setContent(new KyLin_GetFileContent().get_file_content(new KyLinExpression().getExpression(content , kylinRuntime)), kylinRuntime);
            value.setIs_public(true);
            return value;
        }
        else if (funcName.equals("java_runtime")) {
            KyLinValue value = new KyLinValue();
            value.setType("string");
            value.setContent(mainApp.jarDirectory, kylinRuntime);
            value.setIs_public(true);
            return value;
        }
        else if (funcName.equals("length"))
        {
            String type = TypeOf.typeOf(content , kylinRuntime);
            if (type.equals("list")) {
                //System.out.println(Arrays.toString(baseFunction.getValueContent(content, kylinRuntime).toString().split(",(?![^(]*\\))")));
                ArrayList<KyLinValue> arrayList = ((KyLinList) baseFunction.getValueContent(content , kylinRuntime)).arrayList;
                KyLinValue value = new KyLinValue();
                value.setType("list");
                value.setContent(arrayList.size(), kylinRuntime);
                value.setIs_public(true);
                return value;
            }
            else {
                KyLinValue value = new KyLinValue();
                value.setType("num");
                value.setContent(new KyLinExpression().getExpression(content , kylinRuntime).length(), kylinRuntime);
                value.setIs_public(true);
                return value;
            }
        }
        else if (funcName.equals("pow")) {
            double a = Double.parseDouble(new KyLinExpression().getExpression(split[0] , kylinRuntime));
            double b = Double.parseDouble(new KyLinExpression().getExpression(split[1] , kylinRuntime));
            KyLinValue value = new KyLinValue();
            value.setType("num");
            value.setContent(Math.pow(a,b), kylinRuntime);
            value.setIs_public(true);
            return value;
        }
        else if (funcName.equals("sub"))
        {
            String string = new KyLinExpression().getExpression(split[0] , kylinRuntime);
            double a = Double.parseDouble(new KyLinExpression().getExpression(split[1] , kylinRuntime));
            double b = Double.parseDouble(new KyLinExpression().getExpression(split[2] , kylinRuntime));
            KyLinValue value = new KyLinValue();
            value.setType("num");
            value.setContent(string.substring((int) a , (int) b), kylinRuntime);
            value.setIs_public(true);
            return value;
        }
        else if (funcName.equals("index")) {
            String string = new KyLinExpression().getExpression(split[0] , kylinRuntime);
            String charset = new KyLinExpression().getExpression(split[1] , kylinRuntime);

            //System.out.println(string+" "+charset+" "+string.indexOf(charset));

            KyLinValue value = new KyLinValue();
            value.setType("num");
            value.setContent(string.indexOf(charset), kylinRuntime);
            value.setIs_public(true);
            return value;
        }
        else if (funcName.equals("lastindex")) {
            String string = new KyLinExpression().getExpression(split[0] , kylinRuntime);
            String charset = new KyLinExpression().getExpression(split[1] , kylinRuntime);
            KyLinValue value = new KyLinValue();
            value.setType("string");
            value.setContent(String.valueOf(string.lastIndexOf(charset)), kylinRuntime);
            value.setIs_public(true);
            return value;
        }
        else if (funcName.equals("delete") || funcName.equals("rm")) {
            File file = new File(new KyLinExpression().getExpression(split[0] , kylinRuntime));
            KyLinValue value = new KyLinValue();
            value.setType("string");
            value.setContent(String.valueOf(file.delete()), kylinRuntime);
            value.setIs_public(true);
            return value;
        }
        else if (funcName.equals("get_pointer")) {
            //System.out.println(mainApp.all_kylin_value_pointer);
            return getAddress(content,kylinRuntime);
        }
        else if (funcName.equals("toVal")) {
            KyLinValue value = new KyLinValue();
            value.setType("string");
            value.setIs_public(true);
            //System.out.println(new KyLinExpression().getExpression(content,kylinRuntime));
            value.setContent(mainApp.all_kylin_value_pointer.get(new KyLinExpression().getExpression(content,kylinRuntime)) , kylinRuntime);
            return value;
        }
        else if (funcName.equals("shell_output")) {
            KyLinValue value = new KyLinValue();
            value.setType("string");
            value.setIs_public(true);
            // 执行Shell命令
            Process process = Runtime.getRuntime().exec(new KyLinExpression().getExpression(content , kylinRuntime));
            // 获取命令输出流
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
            // 获取命令错误流
            BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            // 读取输出
            String outputLine;
            StringBuilder stringBuilder = new StringBuilder();
            while ((outputLine = stdInput.readLine()) != null) {
                stringBuilder.append(outputLine);
                stringBuilder.append("\n");
            }
            // 读取错误输出
            while ((outputLine = stdError.readLine()) != null) {
                stringBuilder.append(outputLine);
                stringBuilder.append("\n");
            }
            value.setContent(stringBuilder.toString() , kylinRuntime);
            return value;
        }
        else if (funcName.equals("new_thread")) {
            KyLinValue value = new KyLinValue();
            value.setType("thread");
            value.setIs_public(true);
            String pointer = String.valueOf(baseFunction.getRandomLong());
            Thread t = new Thread(() -> {
                try {
                    new KyLinExpression().getExpression(content , kylinRuntime);
                } catch (Exception e) {
                    KylinRuntimeException kylinRuntimeException =
                            new KylinRuntimeException(e.getMessage() , 0 , true);
                    kylinRuntimeException.PrintErrorMessage(null);
                }
            });
            t.start();
            value.setContent(pointer , kylinRuntime);
            mainApp.all_kylin_thread_map.put(pointer , t);

            mainApp.all_kylin_value_pointer.put(pointer , value);
            return value;
        }
        else if (funcName.equals("get_mouse_point")) {
            KyLinValue value = new KyLinValue();
            value.setIs_public(true);
            Point point = MouseInfo.getPointerInfo().getLocation();

            KyLinList list = new KyLinList();
            list.arrayList = new ArrayList<>();
            Integer x = point.x;
            Integer y = point.y;

            KyLinValue x_v = new KyLinValue();
            x_v.setType("num");
            x_v.setIs_public(true);
            x_v.setContent(x , kylinRuntime);
            KyLinValue y_v = new KyLinValue();
            y_v.setType("num");
            y_v.setIs_public(true);
            y_v.setContent(y , kylinRuntime);

            String pointer_1 = String.valueOf(baseFunction.getRandomLong());
            String pointer_2 = String.valueOf(baseFunction.getRandomLong());
            x_v.setName(pointer_1);
            y_v.setName(pointer_2);

            (kylinRuntime.ValueMap).put(pointer_1 , x_v);
            (kylinRuntime.ValueMap).put(pointer_2 , y_v);

            list.arrayList.add(x_v);
            list.arrayList.add(y_v);
            value.setContent(list , kylinRuntime);
            //System.out.println(value.getPointer());
            value.setType("list");
            return value;
        }
        else if (funcName.equals("toInt")) {
            KyLinValue value = new KyLinValue();
            value.setIs_public(true);
            double d = Double.parseDouble(new KyLinExpression().getExpression(content , kylinRuntime));
            value.setContent((int) d , kylinRuntime);
            return value;
        }
        else {
            return null;
        }
    }
    public static KyLinValue getAddress(String content , KyLinRuntime kylinRuntime) throws Exception {
        KyLinValue value = new KyLinValue();
        value.setType("string");

        KyLinRuntime sub_runtime = kylinRuntime.PublicRuntime;
        if (kylinRuntime.ValueMap.containsKey(content)) {
            KyLinValue k = kylinRuntime.ValueMap.get(content);
            value.setContent(String.valueOf(k.getPointer()), kylinRuntime);
        }
        else if (sub_runtime != null && sub_runtime.ValueMap.containsKey(content)) {
            KyLinValue k = sub_runtime.ValueMap.get(content);
            value.setContent(String.valueOf(k.getPointer()), kylinRuntime);
        }
        else {
            throw new Exception("KyLin Pointer Error: No Pointer");
        }
        value.setIs_public(true);
        return value;
    }
}
