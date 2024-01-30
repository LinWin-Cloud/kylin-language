package api;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class KyLin_GetFileContent
{
    public String get_file_content(String path) throws Exception {
        File file = new File(path);
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        StringBuilder stringBuilder = new StringBuilder();

        while (true)
        {
            String line = bufferedReader.readLine();
            if (line == null) {
                break;
            }

            stringBuilder.append(line);
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
}
