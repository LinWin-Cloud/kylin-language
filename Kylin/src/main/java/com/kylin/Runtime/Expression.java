package com.kylin.Runtime;

import com.kylin.Exception.RuntimeError;
import com.kylin.Main;

import java.util.*;
import java.math.BigDecimal;

public class Expression {
    public static String getExpression(String input, int line) {
        input = input.trim();
        String[] parts = input.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");

        StringBuilder sb = new StringBuilder();
        double result = Double.NaN;

        for (String part : parts) {
            if (isNumber(part)) {
                if (Double.isNaN(result)) {
                    result = Double.parseDouble(part);
                } else {
                    result = calculate(result, Double.parseDouble(part), '+', line);
                }
            } else {
                sb.append(part);
            }
        }

        if (Double.isNaN(result)) {
            return sb.toString();
        } else {
            return calculate(result, 0, '+', line) + sb.toString();
        }
    }

    private static boolean isNumber(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static double calculate(double num1, double num2, char operator, int line) {
        switch (operator) {
            case '+':
                return num1 + num2;
            case '-':
                return num1 - num2;
            case '*':
                return num1 * num2;
            case '/':
                return num1 / num2;
            default:
                MainRuntime.sendSyntaxError("Unsupported operators", line);
                return 0.0;
        }
    }
}
