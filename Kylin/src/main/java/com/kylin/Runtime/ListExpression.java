package com.kylin.Runtime;

import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArrayList;

public class ListExpression {
    public void def_list_expression(String name,String code , int line , MainRuntime mainRuntime)
    {
        try
        {
            String[] tokens = code.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
            CopyOnWriteArrayList<String> copyOnWriteArrayList = new CopyOnWriteArrayList<>(Arrays.asList(tokens));
            if (mainRuntime.ValueMap.containsKey(name))
            {
                MainRuntime.sendSyntaxError("Value: '"+name+"' was Defined.",line);
            }
            mainRuntime.ListMap.put(name , copyOnWriteArrayList);
        }
        catch (Exception exception)
        {
            MainRuntime.sendSyntaxError(exception.getMessage() , line);
        }
    }
}
