package Program;

import Function.TypeOf;
import api.KyLin_GetFileContent;
import main.baseFunction;
import main.mainApp;

import java.io.File;
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
    };
    public static boolean isUseFunction(String expression , KyLinRuntime kylinRuntime) {
        try {
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
        String funcName = expression.substring(0,expression.indexOf("(")).trim();
        String content = expression.substring(expression.indexOf("(")+1 , expression.lastIndexOf(")")).trim();
        String[] split = content.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)(?=([^\\(]*\\([^\\)]*\\))*[^\\)]*$)");

        String keyword = "";
        if (funcName.equals(KylinKeyWord[0]) || keyword.equals(KylinKeyWord[0])) {
            KyLinValue value = new KyLinValue();
            value.setType("string");
            value.setContent(baseFunction.getTime() , kylinRuntime);
            value.setIs_public(true);

            return value;
        }else if (funcName.equals(KylinKeyWord[1]) || keyword.equals(KylinKeyWord[1])) {
            KyLinValue value = new KyLinValue();
            value.setType("string");
            value.setContent(baseFunction.input(new KyLinExpression().getExpression(split[0] , kylinRuntime)) , kylinRuntime);
            value.setIs_public(true);

            return value;
        }
        else if (funcName.equals(KylinKeyWord[2]) || keyword.equals(KylinKeyWord[2])) {
            KyLinValue value = new KyLinValue();
            value.setType("string");
            value.setContent(System.getProperty("os.name") , kylinRuntime);
            value.setIs_public(true);
            return value;
        }
        else if (funcName.equals(KylinKeyWord[3]) || keyword.equals(KylinKeyWord[3])) {
            KyLinValue value = new KyLinValue();
            value.setType("string");
            value.setContent(System.getProperty("user.dir") , kylinRuntime);
            value.setIs_public(true);
            return value;
        }
        else if (funcName.equals(KylinKeyWord[4]) || keyword.equals(KylinKeyWord[4])) {
            KyLinValue value = new KyLinValue();
            value.setType("num");
            value.setContent(System.currentTimeMillis() , kylinRuntime);
            value.setIs_public(true);
            return value;
        }
        else if (funcName.equals(KylinKeyWord[5]) || keyword.equals(KylinKeyWord[5])) {
            KyLinValue value = new KyLinValue();
            value.setType("bool");
            value.setContent(new KyLinBoolean().isBool(content,kylinRuntime) , kylinRuntime);
            value.setIs_public(true);
            return value;
        }
        else if (funcName.equals(KylinKeyWord[6]) || keyword.equals(KylinKeyWord[6])) {
            KyLinValue value = new KyLinValue();
            value.setType("string");
            value.setContent(TypeOf.typeOf(content , kylinRuntime), kylinRuntime);
            value.setIs_public(true);
            return value;
        }
        else if (funcName.equals(KylinKeyWord[7]) || keyword.equals(KylinKeyWord[7])) {
            KyLinValue value = new KyLinValue();
            value.setType("bool");
            value.setContent(new File(new KyLinExpression().getExpression(content , kylinRuntime)).exists(), kylinRuntime);
            value.setIs_public(true);
            return value;
        }
        else if (funcName.equals(KylinKeyWord[8]) || keyword.equals(KylinKeyWord[8])) {
            KyLinValue value = new KyLinValue();
            value.setType("string");
            value.setContent(new KyLin_GetFileContent().get_file_content(new KyLinExpression().getExpression(content , kylinRuntime)), kylinRuntime);
            value.setIs_public(true);
            return value;
        }
        else if (funcName.equals(KylinKeyWord[9]) || keyword.equals(KylinKeyWord[9])) {
            KyLinValue value = new KyLinValue();
            value.setType("string");
            value.setContent(mainApp.jarDirectory, kylinRuntime);
            value.setIs_public(true);
            return value;
        }
        else if (funcName.equals(KylinKeyWord[10]) || keyword.equals(KylinKeyWord[10]))
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
        else if (funcName.equals(KylinKeyWord[11]) || keyword.equals(KylinKeyWord[11])) {
            double a = Double.parseDouble(new KyLinExpression().getExpression(split[0] , kylinRuntime));
            double b = Double.parseDouble(new KyLinExpression().getExpression(split[1] , kylinRuntime));
            KyLinValue value = new KyLinValue();
            value.setType("num");
            value.setContent(Math.pow(a,b), kylinRuntime);
            value.setIs_public(true);
            return value;
        }
        else if (funcName.equals(KylinKeyWord[12]) || keyword.equalsIgnoreCase(KylinKeyWord[12]))
        {
            String string = new KyLinExpression().getExpression(split[0] , kylinRuntime);
            int a = Integer.parseInt(new KyLinExpression().getExpression(split[1] , kylinRuntime));
            int b = Integer.parseInt(new KyLinExpression().getExpression(split[2] , kylinRuntime));
            KyLinValue value = new KyLinValue();
            value.setType("num");
            value.setContent(string.substring(a , b), kylinRuntime);
            value.setIs_public(true);
            return value;
        }
        else if (funcName.equals(KylinKeyWord[13]) || keyword.equals(KylinKeyWord[13])) {
            String string = new KyLinExpression().getExpression(split[0] , kylinRuntime);
            String charset = new KyLinExpression().getExpression(split[1] , kylinRuntime);
            KyLinValue value = new KyLinValue();
            value.setType("num");
            value.setContent(string.indexOf(charset), kylinRuntime);
            value.setIs_public(true);
            return value;
        }
        else if (funcName.equals(KylinKeyWord[14]) || keyword.equals(KylinKeyWord[14])) {
            String string = new KyLinExpression().getExpression(split[0] , kylinRuntime);
            String charset = new KyLinExpression().getExpression(split[1] , kylinRuntime);
            KyLinValue value = new KyLinValue();
            value.setType("string");
            value.setContent(String.valueOf(string.lastIndexOf(charset)), kylinRuntime);
            value.setIs_public(true);
            return value;
        }
        else if (funcName.equals(KylinKeyWord[15]) || keyword.equals(KylinKeyWord[15])) {
            File file = new File(new KyLinExpression().getExpression(split[0] , kylinRuntime));
            KyLinValue value = new KyLinValue();
            value.setType("string");
            value.setContent(String.valueOf(file.delete()), kylinRuntime);
            value.setIs_public(true);
            return value;
        }
        else {
            return null;
        }
    }
}
