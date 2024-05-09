package main;

public class PathLoader {
    public void LoadNewPath() {
        mainApp.import_lib_value.put("math","util.math");
        mainApp.import_lib_value.put("linwinhttp","net.http.linwinshs.httpserver");
        mainApp.import_lib_value.put("file","io.file");
    }
    public static String getLibName(String name) {
        return mainApp.import_lib_value.getOrDefault(name, name);
    }
}
