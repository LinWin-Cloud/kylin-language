package KylinException;

import Program.KyLinRuntime;
import main.mainApp;

public class KylinRuntimeException {
    private String ErrMessage;
    private boolean isExit;
    private int line;
    public KylinRuntimeException(String message , int line , boolean exit) {
        this.ErrMessage = message;
        this.isExit = exit;
        this.line = line;
    }
    public String getErrMessage() {
        return this.ErrMessage;
    }
    public void PrintErrorMessage(KyLinRuntime kylinRuntime) {
        System.out.println();
        System.out.println("\u001B[31m" + "KylinRuntimeException: `"+this.ErrMessage+"`" + "\u001B[0m");
        System.out.println("\u001B[31m" + "    Error code: "+kylinRuntime.code.get(kylinRuntime.codeLine) + "\u001B[0m");
        System.out.println("\u001B[31m" + "    At line: "+(kylinRuntime.codeLine+1) + "\u001B[0m");
        System.out.println("\u001B[31m" + "    At Runtime: "+kylinRuntime.name + "\u001B[0m");
        System.out.println("\u001B[31m" + "    At Time: "+main.baseFunction.getTime() + "\u001B[0m");
        System.out.println("\u001B[31m" + "    Boot File: "+ mainApp.file.getAbsolutePath() + "\u001B[0m");
        System.out.println();
        if (this.isExit) {
            System.exit(1);
        }
    }
}
