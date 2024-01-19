package personal.mstall.main.util;

public final class OSUtils {
    public enum OS {
        WINDOWS,
        OSX,
        LINUX
    }
    
    public static final String WINDOWS_BASE_DIR = "/AppData/Local/";
    public static final String OSX_BASE_DIR = "/Library/ApplicationSupport/";
    public static final String LINUX_BASE_DIR = "/.config/";

    public static final String DOCUMENTS_DIR = "/Documents/";

    private static OS os = null;
    private static String userHome = null;

    public static OS getOS() {
        if (os == null) 
            os = findOS();
        return os;
    }

    private static String getUserHome() {
        if (userHome == null)
            userHome = System.getProperty("user.home");
        return userHome;
    }

    public static String documentsDir() {
        OSUtils.OS os = OSUtils.getOS();

        String dirPath = DOCUMENTS_DIR;

        if (os == OS.WINDOWS)
            dirPath.replaceAll("/", "\\\\");
        
        return dirPath;
    }

    public static String baseDir() {
        OS os = getOS();

        String dirPath = null;

        switch (os) {
            case WINDOWS:
                dirPath = getUserHome() + WINDOWS_BASE_DIR;

                // The fucking quadruple backslash makes an appearance here because why not
                dirPath.replaceAll("/", "\\\\");
                break;
            case OSX:
                dirPath = getUserHome() + OSX_BASE_DIR;
                break;
            case LINUX:
                dirPath = getUserHome() + LINUX_BASE_DIR;
                break;
        }
        return dirPath;
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
