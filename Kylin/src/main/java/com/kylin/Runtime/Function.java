package com.kylin.Runtime;

import com.kylin.Main;
import com.sun.xml.internal.bind.v2.runtime.property.StructureLoaderBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Function {
    public ArrayList<String> FunctionValue = new ArrayList<>();
    public ArrayList<String> FunctionCode = new ArrayList<>();
    public String FunctionName;
    public int start;
    public int end;
    public String exec(String input) throws Exception {
        String[] inValue = input.split(",");
        StringBuffer stringBuffer = new StringBuffer("");
        for (int i = 0 ; i < inValue.length ; i++) {
            String j = inValue[i].trim();
            
        }

        for (String key: MainRuntime.execFunctionHashMap.keySet()) {
            Value value = new Value();
            value.name = "$"+key;
            value.value = null;
            MainRuntime.value.put(FunctionName,value);
        }

        String Result = "";
        for (int i = start ; i < Main.code.size() ; i++) {
            String code = Main.code.get(i);
            MainRuntime.exec(code,i);
            if (i == end) {
                break;
            }
            if (code.startsWith("return ")) {
                Result = Expression.getExpression(code.substring(7),i);
                break;
            }
        }
        return Result;
    }
}
