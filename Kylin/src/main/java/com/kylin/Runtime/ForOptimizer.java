package com.kylin.Runtime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ForOptimizer {
    public String Optimizer(String code,MainRuntime mainRuntime,int ForNumber) {
        StringBuffer stringBuffer = new StringBuffer();
        ArrayList<String> stringArrayList = new ArrayList<>(Arrays.asList(code.split("\n")));
        HashMap<String , Integer> indexMap = new HashMap<>();
        String[] split = code.split("\n");
        for (int i = 0 ; i < split.length ; i++)
        {
            if (split[i].startsWith("for")) {
                indexMap.put(split[i] , i);
                continue;
            }
            if (split[i].startsWith("end_for")) {
                indexMap.put(split[i] , i);
                continue;
            }
        }
        System.out.println(code);
        return stringBuffer.toString();
    }
    private void ForNext(String code) {

    }
}
