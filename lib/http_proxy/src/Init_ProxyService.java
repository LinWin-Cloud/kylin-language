
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class Init_ProxyService {
    public void init(String[] args) {
        ProxyService.ProxyPort = Integer.parseInt(args[0]);
        ProxyService.ProxyUrl = args[1];
        ProxyService.version = "2.0";
    }
    public static String getFileContent(String name)
    {
        try
        {
            File file = new File(name);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;
            String tmp = "";

            while ((line = bufferedReader.readLine()) != null)
            {
                tmp = tmp + line;
            }
            bufferedReader.close();
            fileReader.close();
            return tmp;
        }
        catch (Exception exception)
        {
            return null;
        }
    }
}
