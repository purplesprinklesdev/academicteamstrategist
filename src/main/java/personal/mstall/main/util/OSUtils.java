package personal.mstall.main.util;

public final class OSUtils {
    public enum OS {
        WINDOWS,
        OSX,
        LINUX
    }

    private static OS os = null;
    private static String userHome = null;

    public static OS getOS() {
        if (os == null) 
            os = findOS();
        return os;
    }

    public static String getUserHome() {
        if (userHome == null)
            userHome = System.getProperty("user.home");
        return userHome;
    }

    private static OS findOS() {
        String osName = System.getProperty("os.name");

        osName = osName.toLowerCase();

        if (osName.contains("win"))
            return OS.WINDOWS;
        if (osName.contains("mac"))
            return OS.OSX;
        if (osName.contains("linux"))
            return OS.LINUX;
        return null;
    }
}
