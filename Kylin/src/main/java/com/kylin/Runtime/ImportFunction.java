package com.kylin.Runtime;

import java.io.File;

public class ImportFunction {
    public String Lib = "../lib/";

    public void Import(MainRuntime mainRuntime , String lib) throws Exception {
        File importFile = new File(this.Lib + lib + ".ky");
        if (!importFile.exists()) {
            throw new Exception("Can not find Module: "+lib);
        }
    }
}
