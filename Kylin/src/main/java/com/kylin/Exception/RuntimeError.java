package com.kylin.Exception;

import java.util.Date;

public class RuntimeError {
    private String message;
    private String time;
    private String file;
    private int line;

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTime() {
        Date date = new Date(System.currentTimeMillis());
        this.time = String.valueOf(date.getTime());
    }
    public void setLine(int line) {
        this.line = line;
    }

    public void setFile(String path) {
        this.file = path;
    }

    public String getError() {
        return "\nkylin.exception.RuntimeError: "+message+"\n    Time: "+time+"\n    Path: "+file+"\n    At Line: "+line;
    }
}
