package Function;

import java.awt.*;
import java.awt.event.KeyEvent;

public class RobotOptions {
    public static void enterKey(String key) throws Exception {
        Robot robot = new Robot();
        int keyCode = KeyEvent.getExtendedKeyCodeForChar(key.charAt(0));
        //System.out.println("Key to press: " + key + " (" + keyCode + ")");
        robot.keyPress(keyCode);
        //Thread.sleep(100);
        robot.keyRelease(keyCode);
    }
}
