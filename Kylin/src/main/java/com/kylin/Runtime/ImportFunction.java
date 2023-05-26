package com.kylin.Runtime;

import program.value.ExecFunction;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class ImportFunction {
    public String Lib = "../lib/";

    public void Import(MainRuntime mainRuntime , String lib) throws Exception {
        File importFile = new File(this.Lib + "/" + lib.replace(".","/") + ".ky");
        if (!importFile.exists() || !importFile.isFile()) {
            throw new Exception("Can not find Module: "+lib);
        }
        if (!importFile.canRead()) {
            throw new Exception("Can not read target Module: "+lib);
        }
        else {
            FileReader fileReader = new FileReader(importFile);
            MainRuntime importMainRuntime = new MainRuntime(lib);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            ArrayList<String> code = new ArrayList<>();
            while (true)
            {
                String line = bufferedReader.readLine();
                if (line == null)
                {
                    break;
                }
                code.add(line);
            }
            bufferedReader.close();
            importMainRuntime.code = code;
            importMainRuntime.run();
            for (ExecFunction execFunction : importMainRuntime.execFunctionHashMap.values())
            {
                mainRuntime.execFunctionHashMap.put(this.getLibName(importFile.getName())+"."+execFunction.getName() , execFunction);
            }
        }
    }
    private String getLibName(String str) {
        try {
            return str.substring(0 , str.lastIndexOf("."));
        }catch (Exception exception) {
            return str;
        }
    }
}
