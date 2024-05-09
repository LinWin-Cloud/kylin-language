package Program;

import main.baseFunction;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;

public class ImportLib {
    public static void lib_import(String expression , KyLinRuntime kylinRuntime) throws Exception
    {
        //System.out.println(mainApp.jarDirectory);
        String path = main.mainApp.jarDirectory + "/../lib/";
        if ("httpserver".equals(expression)) {
            path += "net/http/linwinshs/";
        }
        
        File result = searchFile(path, expression.replace(".","/") , kylinRuntime);
        if (result != null) {
            KyLinRuntime import_kylin = new KyLinRuntime(result.getName());
            import_kylin.code = baseFunction.getScript(result.getAbsolutePath());
            import_kylin.code_file = result;
            import_kylin.run();
            for (KyLinFunction kylinFunction : import_kylin.FunctionMap.values()) {
                //System.out.println(kylinFunction.name);
                kylinRuntime.FunctionMap.put(main.baseFunction.getLastName(result.getName())+"."+kylinFunction.name , kylinFunction);
            }
            kylinRuntime.classMap.putAll(import_kylin.classMap);
            //System.out.println(kylinRuntime.FunctionMap.keySet());
        }else {
            throw new Exception("No module: "+expression);
        }
    }
    

    public static File searchFile(String basePath, String searchString , KyLinRuntime runtime) {
        File code_file = null;
        File file = new File(basePath + searchString + ".ky");
        if (file.isFile()) {
            return file;
        }else {
            return null;
        }
    }

    public static String get_file_folder(String file){
        // 创建Path对象
        Path filePath = Paths.get(file);
        
        // 获取文件的绝对路径
        Path absolutePath = filePath.toAbsolutePath().normalize();
        
        // 获取文件所在的文件夹
        Path folderPath = absolutePath.getParent();
        
        // 如果文件夹路径存在，返回文件夹路径，否则返回null
        return folderPath.toString();
    }
}
