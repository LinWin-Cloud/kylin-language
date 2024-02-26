package Program;

import main.baseFunction;
import main.mainApp;

import java.io.File;

public class ImportLib {
    public static void lib_import(String expression , KyLinRuntime kylinRuntime) throws Exception
    {
        //System.out.println(mainApp.jarDirectory);
        String path = main.mainApp.jarDirectory + "/../lib/";
        if (expression.equals("httpserver")) {
            path += "net/http/linwinshs/";
        }
        File result = searchFile(path, expression.replace(".","/"));
        if (result != null) {
            KyLinRuntime import_kylin = new KyLinRuntime(result.getName());
            import_kylin.code = baseFunction.getScript(result.getAbsolutePath()); //把代码加载进入运行环境
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

    public static File searchFile(String basePath, String searchString) {
    	/**
	     * 这段代码是ChatGPT写的，我大概检查了一下，确实可以运行
	     *
	     * The ChatGPT was written these code and these code can run very well.
	     */
        File file = new File(basePath+searchString+".ky");
        if (file.isFile()) {
            return file;
        }else {
            return null;
        }
    }
}
