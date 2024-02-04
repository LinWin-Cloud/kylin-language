package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

public class SimpleJson {
    private String FilePath;

    public void setFile(String FilePath) {
        File file = new File(FilePath);
        if (!file.exists() || !file.isFile()) {
            throw new RuntimeException("CAN NOT FIND CONFIG FILE: "+FilePath);
        }
        else {
            this.FilePath = FilePath;
        }
    }
    public String get(String object) {
        return this.readJson(this.FilePath,object);
    }
    private String readJson(String filePath,String value) {
        //read and get the json files content.
        try {
            File file = new File(filePath);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;

            value = "\"" + value + "\"";
            String getValue = null;

            while ((line = bufferedReader.readLine()) != null)
            {
                if (line.contains(value))
                {
                    line = line.substring(line.indexOf(value)+value.length());
                    int e = line.indexOf("\"");
                    if (e != -1) {
                        getValue = line.substring(e+1,line.lastIndexOf("\""));
                        break;
                    }
                }
            }
            return getValue;
        }catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }
}
