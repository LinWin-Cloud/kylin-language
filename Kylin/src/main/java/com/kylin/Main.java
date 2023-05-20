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
import java.util.Scanner;

public class Main {
    public static File resource;
    public static HashMap<String , MainRuntime> runtimeMap = new HashMap<>();

    public static void main(String[] args)
    {
        if (args.length == 0)
        {
            System.out.println("kylin [resource file] ");
            System.exit(1);
        }
        if (args[0].equals("-console")) {
            MainRuntime mainRuntime = new MainRuntime("main");
            mainRuntime.name = "main";
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.print("Kylin> ");
                String code = scanner.nextLine();
                if (code.equals("exit")) {
                    break;
                }
                try {
                    mainRuntime.exec(code,"");
                }catch (Exception exception) {
                    System.out.println(exception.getMessage());
                }
            }
            System.exit(0);
        }
        resource = new File(args[0]);
        if (!resource.exists())
        {
            System.out.println("[ERROR] Cannot find target file: "+resource.getAbsolutePath()+".");
            System.exit(1);
        }
        if (resource.exists() && resource.isDirectory())
        {
            System.out.println("[ERROR] Cannot start a directory.");
            System.exit(1);
        }
        try
        {
            FileReader fileReader = new FileReader(resource);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            MainRuntime mainRuntime = new MainRuntime(resource.getName());
            while (true) {
                String line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }
                line = line.trim();
                mainRuntime.code.add(line);
            }
            mainRuntime.name = resource.getName();
            long start = System.currentTimeMillis();
            mainRuntime.run();
            long end = System.currentTimeMillis();
            //System.out.println(end-start + " ms");
        }
        catch (Exception exception) {
            MainRuntime.sendRuntimeError(exception.getMessage(),0);
            System.exit(1);
        }
    }
}