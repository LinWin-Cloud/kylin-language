package com.kylin.Runtime;

public class Type {
    public static String GetType(String value) {
        String type = "string";
        if (value.startsWith("[") && value.endsWith("]"))
        {
            type = "list";
        }
        else {
            try {
                Integer.parseInt(value);
            }
            catch (Exception exception) {
                try {
                    Double.parseDouble(value);
                }
                catch (Exception ignored) {
                }
            }
        }
        return type;
    }
}
