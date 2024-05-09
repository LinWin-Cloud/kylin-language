package Program;

import KylinException.KylinRuntimeException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KyLinBoolean {
    public boolean isBool(String str , KyLinRuntime kylinRuntime) throws Exception
    {
        try {
            return evaluateBooleanExpression(str , kylinRuntime);
        }catch (Exception exception) {
            KylinRuntimeException kylinRuntimeException = new KylinRuntimeException(exception.getMessage() , kylinRuntime.codeLine , true);
            kylinRuntimeException.setStackTrace(exception.getStackTrace());
            return false;
        }
    }

    public static boolean evaluateBooleanExpression(String expression , KyLinRuntime kyLinRuntime) throws Exception {
        if (expression.contains("==")) {
            String[] operands = expression.split("==");
            return new KyLinExpression().getExpression(operands[0].trim() , kyLinRuntime).equals(new KyLinExpression().getExpression(operands[1].trim(),kyLinRuntime));
        }
        else if (expression.equalsIgnoreCase("ture")) {
            return true;
        }
        else if (expression.equalsIgnoreCase("false")) {
            return false;
        }
        else if (expression.contains("!=")) {
            String[] operands = expression.split("!=");
            return !new KyLinExpression().getExpression(operands[0].trim() , kyLinRuntime).equals(new KyLinExpression().getExpression(operands[1].trim(),kyLinRuntime));
        } else if (expression.contains(">=")) {
            String[] operands = expression.split(">=");
            return Double.parseDouble(operands[0].trim()) >= Double.parseDouble(operands[1].trim());
        } else if (expression.contains("<=")) {
            String[] operands = expression.split("<=");
            return Double.parseDouble(operands[0].trim()) <= Double.parseDouble(operands[1].trim());
        } else if (expression.contains(">")) {
            String[] operands = expression.split(">");
            return Double.parseDouble(operands[0].trim()) > Double.parseDouble(operands[1].trim());
        } else if (expression.contains("<")) {
            String[] operands = expression.split("<");
            return Double.parseDouble(operands[0].trim()) < Double.parseDouble(operands[1].trim());
        } else {
            try {
                //System.out.println(new KyLinExpression().getExpression(expression , kyLinRuntime));
                return Boolean.parseBoolean(new KyLinExpression().getExpression(expression , kyLinRuntime));
            }catch (Exception exception) {
                throw new IllegalArgumentException("Expression error: "+exception.getMessage());
            }
        }
    }
}
