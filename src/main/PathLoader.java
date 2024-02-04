package main;

import Program.KyLinRuntime;

public class PathLoader {
    public void LoadNewPath() {
        /**
         * Load all the path value to the hashmap
         */
        mainApp.import_lib_value.put("math","util.math");
        mainApp.import_lib_value.put("linwinhttp","net.http.linwinshs.httpserver");
        mainApp.import_lib_value.put("file","io.file");
    }
    public static String getLibName(String name) {
        return mainApp.import_lib_value.getOrDefault(name, name);
    }
}
