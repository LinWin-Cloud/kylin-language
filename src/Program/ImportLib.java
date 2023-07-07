package Program;

import main.baseFunction;

import java.io.File;

public class ImportLib {
    public static void lib_import(String expression , KylinRuntime kylinRuntime) throws Exception
    {
        String path = main.mainApp.jarDirectory + "/../lib/";
        File result = searchFile(path, expression);
        if (result != null) {
            KylinRuntime import_kylin = new KylinRuntime(result.getName());
            import_kylin.code = baseFunction.getScript(result.getAbsolutePath()); //把代码加载进入运行环境
            import_kylin.run();
            for (KylinFunction kylinFunction : import_kylin.FunctionMap.values()) {
                kylinRuntime.FunctionMap.put(result.getName()+"."+kylinFunction.name , kylinFunction);
            }
        }else {
            throw new Exception("No module: "+expression);
        }
    }

    public static File searchFile(String basePath, String searchString) {
    	/**
	     * 这段代码是ChatGPT写的，我大概检查了一下，确实可以运行
	     *
	     * The ChatGPT was written these code and these code can run very well.
	     */    
	     
        File baseDir = new File(basePath);

        if (!baseDir.isDirectory()) {
            return null;
        }

        File[] files = baseDir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().equals(searchString + ".ky")) {
                    return file;
                } else if (file.isDirectory()) {
                    File result = searchFile(file.getAbsolutePath(), searchString);
                    if (result != null) {
                        return result;
                    }
                }
            }

            for (File file : files) {
                if (file.isFile() && file.getName().startsWith(searchString)) {
                    return file;
                }
            }
        }

        return null;
    }
}
