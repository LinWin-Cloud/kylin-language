package com.kylin.Runtime;

import sun.awt.X11.XSystemTrayPeer;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class For {
    private int range = 0;
    public void setRange(int range) {
        this.range = range;
    }
    private int ForNumber = 0;
    public void setForNumber(int number) {
        this.ForNumber = number;
    }
    private static final String FOR_PATTERN = "for \\((\\d+)\\)";
    private static final String END_FOR_PATTERN = "end_for";
    public void ForDo(String code, int start,MainRuntime mainRuntime)
            throws Exception
    {
        /**
        String[] split = code.split("(for\\s*\\(\\s*\\d+\\s*\\))(.*?)(end_for)");
        for (String i : split)
        {
            System.out.println("--------------------");
            System.out.println(i);
        }*/
        String getOptimizer = new ForOptimizer().Optimizer(code,mainRuntime,ForNumber);
        for (int i = 0 ; i < this.range ;i++)
        {
            for (String s : getOptimizer.split("\n"))
            {
                mainRuntime.exec(s , "");
            }
        }
    }
    private void parse(String code) {

    }
}
