package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class baseFunction {
    public static ArrayList<String> getScript(String path) throws Exception {
        FileReader fileReader = new FileReader(path);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        ArrayList<String> arrayList = new ArrayList<>();
        while (true)
        {
            String line = bufferedReader.readLine();
            if (line == null) {
                break;
            }
            arrayList.add(line);
        }
        bufferedReader.close();
        fileReader.close();
        return arrayList;
    }
    public static boolean isPublic(String expression) throws Exception {
        if (expression.toLowerCase().equals("public")) {
            return true;
        }else if(expression.toLowerCase().equals("private")) {
            return false;
        }else {
            throw new Exception("Syntax Error");
        }
    }
}
