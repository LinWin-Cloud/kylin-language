package com.kylin.Runtime;

import com.kylin.Exception.RuntimeError;
import com.kylin.Main;

import java.util.*;
import java.math.BigDecimal;

public class Expression {
    public static String getExpression(String input,int line) {
        input = input.trim();
        String[] InputSplit = input.split("(?=\\+)|(?=-)|(?=/)|(?=\\*)| ");
        List<String> list = new ArrayList<>();

        for (int i = 0 ; i < InputSplit.length ; i++) {
            String n = InputSplit[i].trim();
            if (n.equals(" ")) {
                continue;
            }
            if (n.equals("")) {
                continue;
            }
            else {
                Value value = MainRuntime.value.get(n);
                if (value == null) {
                    list.add(n);
                }
                else {
                    list.add(value.value);
                }
            }
        }
        for (int i = 0 ; i < list.size() ; i++) {
            String FirstProject = list.get(i);
            String charset = list.get(i+1);
            String SecondProject = list.get(i+2);
            
        }
    }
}
