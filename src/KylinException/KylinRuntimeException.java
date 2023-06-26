package KylinException;

import Program.KylinRuntime;

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
    public void PrintErrorMessage(KylinRuntime kylinRuntime) {
        System.out.println("KylinRuntimeException: `"+this.ErrMessage+"`");
        System.out.println("    Error code: "+kylinRuntime.code.get(kylinRuntime.codeLine));
        System.out.println("    At line: "+(kylinRuntime.codeLine+1));
        System.out.println("    At Runtime: "+kylinRuntime.name);
        if (this.isExit) {
            System.exit(1);
        }
    }
}
