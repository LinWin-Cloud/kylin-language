package Web.Safe;

import main.Main;

public class DDOS {
    private int AllowRequests = 2^31-1;
    private final Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            while (true) {
                try {
                    Main.RequestsIP.clear();
                    Thread.sleep(10 * 1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    });
    public void setAllowRequests(int AllowRequests)
    {
        this.AllowRequests = AllowRequests;
    }
    public void DDOS_Safe_Run() {
        this.thread.start();
    }
}
