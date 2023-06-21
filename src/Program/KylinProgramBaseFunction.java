package Program;

public class KylinProgramBaseFunction {
    public static boolean isProgramBaseFunction(String code) {
        try {
            String function = code.substring(0 , code.indexOf("(")).trim();
            System.out.println(function);
            return true;
        }catch (Exception exception) {
            return false;
        }
    }
}
