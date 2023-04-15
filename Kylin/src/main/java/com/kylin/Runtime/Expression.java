package com.kylin.Runtime;

import com.kylin.Exception.RuntimeError;
import com.kylin.Main;

import java.util.*;
import java.math.BigDecimal;

import javax.script.*;

public class Expression {
    public static String getExpression(String input, int line) throws Exception {
        input = input.trim();

        String[] split = input.split("(?=\\+)|(?=\\*)|(?=/)|(?=-)| ");
        StringBuffer stringBuffer = new StringBuffer("");

        for (int i = 0 ; i < split.length ; i++)
        {
            String s = split[i].trim();

            Value value = MainRuntime.value.get(s);
            Function function = MainRuntime.execFunctionHashMap.get(s);
            if (value != null) {
                stringBuffer.append(value.value);
            }
            if (function != null) {
                stringBuffer.append(function.exec());
            }
            else {
                stringBuffer.append(s);
            }
        }
        String code = stringBuffer.toString();
        code = code.replace("\\\\_"," ");

        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("javascript");
        try
        {
            engine.eval("var tmp = "+code);
            Object object = engine.get("tmp");
            return object.toString();
        }
        catch (Exception exception){
            MainRuntime.sendRuntimeError(exception.getMessage(),line);
            System.exit(1);
            return null;
        }
    }
}
