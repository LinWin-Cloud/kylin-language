package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

public class baseFunction {
    public static ArrayList<String> getScript(String path) throws Exception {
        FileReader fileReader = new FileReader(path);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        ArrayList<String> arrayList = new ArrayList<>();
        while (true)
        {
            String line = bufferedReader.readLine();
            if (line == null) {
                break;
            }
            arrayList.add(line);
        }
        bufferedReader.close();
        fileReader.close();
        return arrayList;
    }
    public static boolean isPublic(String expression) throws Exception {
        if (expression.toLowerCase().equals("public")) {
            return true;
        }else if(expression.toLowerCase().equals("private")) {
            return false;
        }else {
            throw new Exception("Syntax Error");
        }
    }
    public static String getTime() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(year);
        stringBuffer.append(month);
        stringBuffer.append(day);
        stringBuffer.append(hour);
        stringBuffer.append(minute);
        stringBuffer.append(second);
        return stringBuffer.toString();
    }
    public static String input(String text) {
        System.out.print(text);
        Scanner scanner = new Scanner(System.in);
        String in = scanner.nextLine();
        scanner.close();
        return in;
    }
}
