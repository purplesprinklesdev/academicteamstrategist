package personal.mstall.main.util;

public class Debug {
    public static boolean debugModeOn = false;

    public static void PrintOut(String out) {
        if (!debugModeOn)
            return;
        
        System.out.println("DEBUG MESSAGE || T = " + System.currentTimeMillis() + "ms || " + out);
    }
}
