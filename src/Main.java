import KylinException.KylinRuntimeException;

/**
 * @author SuzushimaArisu
 */
public class Main {
    public static void main(String[] args) {
        try {
            main.mainApp.main(args);
        } catch (Exception exception) {
            KylinRuntimeException kylinRuntimeException =
                    new KylinRuntimeException(exception.getMessage() , 0 , true);
            kylinRuntimeException.setStackTrace(exception.getStackTrace());
        }
    }
}