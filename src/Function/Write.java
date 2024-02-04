package Function;

import Program.KyLinExpression;

import java.io.File;
import java.io.FileWriter;

public class Write {
    public void write(String path , String content , boolean isCover) throws Exception {
        File file = new File(path);

        if (file.isDirectory())
        {
            throw new Exception("Target path is a directory");
        }
        else {
            FileWriter fileWriter = new FileWriter(path , isCover);
            fileWriter.write(content);System.out.println(content);
            fileWriter.flush();
            fileWriter.close();
        }
    }
}
