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
    public String[] get_list_expression(String code, int line,MainRuntime mainRuntime)
    {
        try {
            String[] token = code.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
            List<String> stringList = new ArrayList<>();

            for (String i : token) {
                if (mainRuntime.ValueMap.containsKey(i)) {
                    stringList.add(mainRuntime.ValueMap.get(i).getContent().toString());
                    continue;
                }
                stringList.add(i);
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
