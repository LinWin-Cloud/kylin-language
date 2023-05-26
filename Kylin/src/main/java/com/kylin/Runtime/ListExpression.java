package com.kylin.Runtime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ListExpression {
    public void def_list_expression(
            String name,
            String code,
            int line,
            MainRuntime mainRuntime)
    {
        try
        {
            String[] tokens = code.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
            CopyOnWriteArrayList<String> copyOnWriteArrayList = new CopyOnWriteArrayList<>();
            for (String i : tokens)
            {
                i = i.trim();
                if (mainRuntime.ValueMap.containsKey(name))
                {
                    MainRuntime.sendSyntaxError("Value: '"+name+"' was Defined.",line);
                }
                copyOnWriteArrayList.add(Expression.getExString(i , line , mainRuntime));
            }
            mainRuntime.ListMap.put(name , copyOnWriteArrayList);
        }
        catch (Exception exception)
        {
            MainRuntime.sendSyntaxError(exception.getMessage() , line);
        }
    }
    public String[] get_list_expression(String code, int line,MainRuntime mainRuntime)
    {
        try {
            String[] token = code.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
            List<String> stringList = new ArrayList<>();

            for (String i : token) {
                i = i .trim();
                stringList.add(Expression.getExString(i , line, mainRuntime));
            }
            return stringList.toArray(new String[stringList.size()]);
        }
        catch (Exception exception)
        {

            MainRuntime.sendSyntaxError(exception.getMessage() , line);
            return null;
        }
    }
}
