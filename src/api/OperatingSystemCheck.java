package api;

public class OperatingSystemCheck {

    public static String getOperatingSystem() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            return "Windows";
        } else if (os.contains("mac")) {
            return "macOS";
        } else if (os.contains("nix") || os.contains("nux")) {
            return "Linux";
        } else {
            return "unknow";
        }
    }
}
