
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static File resource;
    public static ExecutorService executorService = Executors.newFixedThreadPool(200);

    public static void main(String[] args)
    {
        if (args.length == 0)
        {
            System.out.println("kylinc [resource file]");
            System.exit(1);
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
            ArrayList<String> codeList = new ArrayList<>();
            while (true) {
                String line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }
                line = line.trim();
                codeList.add(line);
            }
        }
        catch (Exception exception) {
            System.out.println(exception.getMessage());
            System.exit(1);
        }
    }
}