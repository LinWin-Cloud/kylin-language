package Program;

import Function.TypeOf;
import KylinException.KylinRuntimeException;
import api.HttpRequests;
import api.KyLin_GetFileContent;
import api.OperatingSystemCheck;
import main.baseFunction;
import main.mainApp;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.*;

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
            "randomInt",                // 23
            "index_list",               // 24
            "isnumber",                 // 25
            "http_requests",            // 26
            "is_app_install",           // 27
            "",                         // 28
    };
    public static boolean isUseFunction(String expression , KyLinRuntime kylinRuntime) {
        try {
            /**
             * [注释补充]
             * 判断是否是可调用的 函数
             *
             */
            //System.out.println(1);
            String funcName = expression.substring(0,expression.indexOf("(")).trim();
            boolean is = false;
            for (String s : new HashSet<String>(Arrays.asList(KylinKeyWord))) {
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
        String funcName = expression.substring(0, expression.indexOf("(")).trim();
        String content = expression.substring(expression.indexOf("(") + 1, expression.lastIndexOf(")")).trim();
        String[] split = content.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)(?=([^\\(]*\\([^\\)]*\\))*[^\\)]*$)");

        KyLinValue value = new KyLinValue();
        switch (funcName) {
            case "is_app_install":
                value.setIs_public(true);
                try {
                    if (OperatingSystemCheck.getOperatingSystem().equals("Windows")) {
                        File f = new File(new KyLinExpression().getExpression(content , kylinRuntime));
                        value.setContent(f.exists() , kylinRuntime);
                    }
                    else {
                        File f = new File("/bin/"+new KyLinExpression().getExpression(content , kylinRuntime));
                        value.setContent(f.exists() , kylinRuntime);
                    }
                }catch (Exception e) {
                    value.setContent(false , kylinRuntime);
                }
                return value;
            case "http_requests":
                value.setIs_public(true);
                value.setContent(new HttpRequests().GET_Requests(new KyLinExpression().getExpression(content, kylinRuntime)), kylinRuntime);
                return value;
            case "isnumber":
                value.setIs_public(true);
                boolean content_3 = false;
                try {
                    double d = Double.parseDouble(new KyLinExpression().getExpression(content, kylinRuntime));
                    int a_3 = (int) d;
                    content_3 = true;
                }catch (Exception exception) {
                    content_3 = false;
                }
                value.setContent(content_3 , kylinRuntime);
                return value;
            case "index_list":
                KyLinList list = (KyLinList) Objects.requireNonNull(KyLinExpression.getValueFromRuntime(split[0].trim(), kylinRuntime)).getContent();
                value.setType("num");
                //System.out.println(split[1]);
                int index = -1;
                int j = 0;
                //System.out.println(list.arrayList);
                String s = new KyLinExpression().getExpression(split[1].trim(), kylinRuntime);
                s = new KyLinExpression().getExpression(s, kylinRuntime);
                for (KyLinValue v : list.arrayList) {
                    //System.out.println(v.getName()+" "+s);
                    if (v.getContent().equals(s)) {
                        index = j;
                        break;
                    }
                    j++;
                }
                //System.out.println(index);
                value.setContent(index, kylinRuntime);
                value.setIs_public(true);
                return value;
            case "getTime":
                value.setType("string");
                value.setContent(baseFunction.getTime(), kylinRuntime);
                value.setIs_public(true);
                return value;
            case "input":
                value.setType("string");
                value.setContent(baseFunction.input(new KyLinExpression().getExpression(split[0], kylinRuntime)), kylinRuntime);
                value.setIs_public(true);
                return value;
            case "randomInt":
                value.setType("num");

                double a = Double.parseDouble(new KyLinExpression().getExpression(split[0], kylinRuntime));
                double b = Double.parseDouble(new KyLinExpression().getExpression(split[1], kylinRuntime));

                Random random = new Random();
                value.setContent(random.nextInt((int) b - (int) a + 1) + ((int) a), kylinRuntime);
                return value;
            case "get_os":
                value.setType("string");
                value.setContent(System.getProperty("os.name"), kylinRuntime);
                value.setIs_public(true);
                return value;
            case "get_path":
                value.setType("string");
                value.setContent(System.getProperty("user.dir"), kylinRuntime);
                value.setIs_public(true);
                return value;
            case "long_time":
                value.setType("num");
                value.setContent(System.currentTimeMillis(), kylinRuntime);
                value.setIs_public(true);
                return value;
            case "bool":
                value.setType("bool");
                value.setContent(new KyLinBoolean().isBool(content, kylinRuntime), kylinRuntime);
                value.setIs_public(true);
                return value;
            case "typeof":
                value.setType("string");
                value.setContent(KyLinExpression.getValueFromRuntime(content, kylinRuntime).getType(), kylinRuntime);
                value.setIs_public(true);
                return value;
            case "file_exists":
                value.setType("bool");
                value.setContent(new File(new KyLinExpression().getExpression(content, kylinRuntime)).exists(), kylinRuntime);
                value.setIs_public(true);
                return value;
            case "get_file_content":
                value.setType("string");
                value.setContent(new KyLin_GetFileContent().get_file_content(new KyLinExpression().getExpression(content, kylinRuntime)), kylinRuntime);
                value.setIs_public(true);
                return value;
            case "java_runtime":
                value.setType("string");
                value.setContent(mainApp.jarDirectory, kylinRuntime);
                value.setIs_public(true);
                return value;
            case "length":
                String type = TypeOf.typeOf(content, kylinRuntime);
                if (type.equals("list")) {
                    //System.out.println(Arrays.toString(baseFunction.getValueContent(content, kylinRuntime).toString().split(",(?![^(]*\\))")));
                    ArrayList<KyLinValue> arrayList = ((KyLinList) baseFunction.getValueContent(content, kylinRuntime)).arrayList;
                    value.setType("list");
                    value.setContent(arrayList.size(), kylinRuntime);
                    value.setIs_public(true);
                    return value;
                } else {
                    value.setType("num");
                    value.setContent(new KyLinExpression().getExpression(content, kylinRuntime).length(), kylinRuntime);
                    value.setIs_public(true);
                    return value;
                }
            case "pow":
                double a_1 = Double.parseDouble(new KyLinExpression().getExpression(split[0], kylinRuntime));
                double b_1 = Double.parseDouble(new KyLinExpression().getExpression(split[1], kylinRuntime));
                value.setType("num");
                value.setContent(Math.pow(a_1, b_1), kylinRuntime);
                value.setIs_public(true);
                return value;
            case "sub":
                String string = new KyLinExpression().getExpression(split[0], kylinRuntime);
                double a_2 = Double.parseDouble(new KyLinExpression().getExpression(split[1], kylinRuntime));
                double b_2 = Double.parseDouble(new KyLinExpression().getExpression(split[2], kylinRuntime));
                value.setType("num");
                //System.out.println(string);

                value.setContent(string.substring((int) a_2, (int) b_2), kylinRuntime);
                value.setIs_public(true);
                return value;
            case "index":
                String string_1 = new KyLinExpression().getExpression(split[0], kylinRuntime);
                String charset = new KyLinExpression().getExpression(split[1], kylinRuntime);

                //System.out.println(string+" "+charset+" "+string.indexOf(charset));
                value.setType("num");
                value.setContent(string_1.indexOf(charset), kylinRuntime);
                value.setIs_public(true);
                return value;
            case "lastindex":
                String strings = new KyLinExpression().getExpression(split[0], kylinRuntime);
                String charsets = new KyLinExpression().getExpression(split[1], kylinRuntime);
                value.setType("string");
                value.setContent(String.valueOf(strings.lastIndexOf(charsets)), kylinRuntime);
                value.setIs_public(true);
                return value;
            case "delete":
                File file = new File(new KyLinExpression().getExpression(split[0], kylinRuntime));
                value.setType("string");
                value.setContent(String.valueOf(file.delete()), kylinRuntime);
                value.setIs_public(true);
                return value;
            case "rm":
                File frm = new File(new KyLinExpression().getExpression(split[0], kylinRuntime));
                value.setType("string");
                value.setContent(String.valueOf(frm.delete()), kylinRuntime);
                value.setIs_public(true);
                return value;
            case "get_pointer":
                //System.out.println(mainApp.all_kylin_value_pointer);
                return getAddress(content, kylinRuntime);
            case "toVal":
                value.setType("string");
                value.setIs_public(true);
                //System.out.println(new KyLinExpression().getExpression(content,kylinRuntime));
                value.setContent(mainApp.all_kylin_value_pointer.get(new KyLinExpression().getExpression(content, kylinRuntime)), kylinRuntime);
                return value;
            case "shell_output":
                value.setType("string");
                value.setIs_public(true);
                // 执行Shell命令
                Process process = Runtime.getRuntime().exec(new KyLinExpression().getExpression(content, kylinRuntime));
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
                value.setContent(stringBuilder.toString(), kylinRuntime);
                return value;
            case "new_thread":
                value.setType("thread");
                value.setIs_public(true);
                String pointer = String.valueOf(baseFunction.getRandomLong());
                Thread t = new Thread(() -> {
                    try {
                        new KyLinExpression().getExpression(content, kylinRuntime);
                    } catch (Exception e) {
                        KylinRuntimeException kylinRuntimeException =
                                new KylinRuntimeException(e.getMessage(), 0, true);
                        kylinRuntimeException.PrintErrorMessage(null);
                    }
                });
                t.start();
                value.setContent(pointer, kylinRuntime);
                mainApp.all_kylin_thread_map.put(pointer, t);

                mainApp.all_kylin_value_pointer.put(pointer, value);
                return value;
            case "get_mouse_point":
                value.setIs_public(true);
                Point point = MouseInfo.getPointerInfo().getLocation();

                KyLinList list_1 = new KyLinList();
                list_1.arrayList = new ArrayList<>();
                Integer x = point.x;
                Integer y = point.y;

                KyLinValue x_v = new KyLinValue();
                x_v.setType("num");
                x_v.setIs_public(true);
                x_v.setContent(x, kylinRuntime);
                KyLinValue y_v = new KyLinValue();
                y_v.setType("num");
                y_v.setIs_public(true);
                y_v.setContent(y, kylinRuntime);

                String pointer_1 = String.valueOf(baseFunction.getRandomLong());
                String pointer_2 = String.valueOf(baseFunction.getRandomLong());
                x_v.setName(pointer_1);
                y_v.setName(pointer_2);

                (kylinRuntime.ValueMap).put(pointer_1, x_v);
                (kylinRuntime.ValueMap).put(pointer_2, y_v);

                list_1.arrayList.add(x_v);
                list_1.arrayList.add(y_v);
                value.setContent(list_1, kylinRuntime);
                //System.out.println(value.getPointer());
                value.setType("list");
                return value;
            case "toInt":
                value.setIs_public(true);
                double d = Double.parseDouble(new KyLinExpression().getExpression(content, kylinRuntime));
                value.setContent((int) d, kylinRuntime);
                return value;
            default:
                return null;
        }
    }
    public static KyLinValue getAddress(String content , KyLinRuntime kylinRuntime) throws Exception {
        KyLinValue value = new KyLinValue();
        value.setType("string");
        KyLinValue v = KyLinExpression.getValueFromRuntime(content, kylinRuntime);
        if (v != null) {
            value.setContent(v.getPointer() , kylinRuntime);
        }
        else {
            throw new Exception("KyLin Pointer Error: No Pointer");
        }
        value.setIs_public(true);
        return value;
    }
}
