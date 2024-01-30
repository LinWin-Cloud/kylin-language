package main;


import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.io.File;
import java.util.HashSet;
import java.util.concurrent.CopyOnWriteArrayList;

public class LogService {
    private int NewFileWrite_Time = 1000 * 60 * 60;
    private FileWriter fileWriter;
    public String fileName;
    public String setNAME;
    public String path;
    public boolean loadOK_1 = false;
    public CopyOnWriteArrayList<String> log_list = new CopyOnWriteArrayList<>();
    public LogService() {
    }
    public void print(String message) {
        PrintStream printStream = new PrintStream(System.out);
        printStream.println(message);
    }
    public void printInfo(String message,String type) {
        this.print("{"+type+"} Message= "+message+" ;["+this.getNowTime()+"]");
    }
    public String getNowTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd.HH");
        java.util.Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }
    private String getNowTime_2() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd.HH:mm:ss");
        java.util.Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }
    private boolean outLog(String logMessage) {
        try{
            File file = new File(this.fileName);
            if (!file.exists() || !file.isFile()) {
                file.createNewFile();
            }
            this.fileWriter.write("{"+this.getNowTime()+"} "+logMessage+"\n");
            this.fileWriter.flush();
            return true;
        }catch (Exception exception){
            exception.printStackTrace();
            return false;
        }
    }
    public void SaveLog(String message) {
        this.log_list.add(message);
    }
    public void setAutoNewLog(int time) {
        this.NewFileWrite_Time = time;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public void setFileName(String fileName) {
        this.fileName = this.path + "/" + fileName + this.getNowTime() + ".log";
        this.setNAME = fileName;
    }
    public void run() {
        try{
            this.loadOK_1 = true;
            Thread logFileWriteThread = new Thread(this.WriteRunnable());
            logFileWriteThread.start();

        }catch (Exception exception){
            exception.printStackTrace();
        }
    }
    public void run_write() {
        while (true)
        {
            try {
                StringBuilder stringBuilder = new StringBuilder();
                for (String i : this.log_list)
                {
                    stringBuilder.append(i);
                    stringBuilder.append("\n");
                }
                this.outLog(stringBuilder.toString());
                this.log_list.clear();
                Thread.sleep(10 * 1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private Runnable WriteRunnable() {
        while (true) {
            try{
                if (this.fileWriter == null){
                    try{
                        this.fileName = this.path + "/" + this.setNAME + this.getNowTime() + ".log";
                        this.fileWriter = new FileWriter(this.fileName,true);
                        continue;
                    }catch (Exception exception){
                        exception.printStackTrace();
                    }
                }
                this.fileName = this.path + "/" + this.setNAME + this.getNowTime() + ".log";
                this.fileWriter = new FileWriter(this.fileName,true);
                Thread.sleep(this.NewFileWrite_Time);
            }catch (Exception exception){
                exception.printStackTrace();
                System.out.println("[ERR] Config Error!");
                System.exit(0);
            }
        }
    }
}