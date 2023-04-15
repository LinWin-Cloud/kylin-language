package com.kylin;

import com.kylin.Exception.RuntimeError;
import com.kylin.Runtime.ExceptionCatch;
import com.kylin.Runtime.MainRuntime;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    public static File resource;
    public static ArrayList<String> code = new ArrayList<>();
    public static HashMap<Integer, RuntimeError> Exception = new HashMap<>();
    public static ArrayList<ExceptionCatch> ExceptionCode = new ArrayList<>();

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("kylin [resource file] ");
            System.exit(1);
        }
        resource = new File(args[0]);
        if (!resource.exists()) {
            System.out.println("[ERROR] Cannot find target file: "+resource.getAbsolutePath()+".");
            System.exit(1);
        }
        if (resource.exists() && resource.isDirectory()) {
            System.out.println("[ERROR] Cannot start a directory.");
            System.exit(1);
        }
        try {
            FileReader fileReader = new FileReader(resource);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while (true) {
                String line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }
                line = line.trim();
                code.add(line);
            }
            MainRuntime.run();
        }

        catch (Exception exception) {
            System.out.println("[ERROR] Read target file error.");
            System.exit(1);
        }
    }
}