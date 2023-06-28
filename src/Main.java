import KylinException.KylinRuntimeException;

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
