package com.kylin.Runtime;

import com.kylin.Main;
import com.sun.xml.internal.bind.v2.runtime.property.StructureLoaderBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Function {
    public ArrayList<Value> FunctionValue = new ArrayList<>();
    public ArrayList<String> FunctionCode = new ArrayList<>();
    public String FunctionName;
    public int start;
    public int end;
    public String exec(String input) throws Exception {
        String[] inValue = input.split(",");
        StringBuffer stringBuffer = new StringBuffer("");
        for (int i = 0 ; i < inValue.length ; i++) {
            String j = inValue[i].trim();
            String getInput = Expression.getExpression(j.substring(j.indexOf("(")+1,j.lastIndexOf(")")),start);
            Value value = FunctionValue.get(i);
            value.value = getInput;
            FunctionValue.set(i,value);
        }

        for (int i = 0 ; i < FunctionValue.size() ; i++) {
            Value value = FunctionValue.get(i);
            MainRuntime.value.put(value.name,value);
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
