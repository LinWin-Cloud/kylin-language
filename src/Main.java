import KylinException.KylinRuntimeException;

/**
 * 家人们，谁懂啊，这个项目前前后后重构好几次，不过这次确实设计的很好了，反正少走了很多弯路
 * 这个项目中有很多代码是 ChatGPT 写的，如果看到操蛋的代码那一定是人工智能的错误 [迫真!]
 * 
 * 这个文件放在这是因为让大家一眼就能知道这个是启动的主类.
 */

public class Main {
    public static void main(String[] args) {
        try {
            
            main.mainApp.main(args);
        }catch (Exception exception) {
            //exception.printStackTrace();
            KylinRuntimeException kylinRuntimeException =
                    new KylinRuntimeException(exception.getMessage() , 0 , true);
            kylinRuntimeException.PrintErrorMessage(null);
        }
    }
}
