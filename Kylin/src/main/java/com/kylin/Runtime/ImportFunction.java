package com.kylin.Runtime;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class ImportFunction {
    public String Lib = "../lib/";

    public void Import(MainRuntime mainRuntime , String lib) throws Exception {
        File importFile = new File(this.Lib + lib + ".ky");
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
            
        }
    }
}
