package personal.mstall.main.util;

import java.io.File;

import jakarta.xml.bind.*;

public class SaveManager {

    public static final Object Load(FileType type) {
        File file = findFile(type.filename);

        if (!file.exists())
            return null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(type.clss);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            return unmarshaller.unmarshal(file);
        } catch (JAXBException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static final boolean Save(Object object, FileType type) {
        File file = findFile(type.filename);

        if (object.getClass() != type.clss)
            return false;

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(type.clss);
            Marshaller marshaller = jaxbContext.createMarshaller();

            marshaller.marshal(object, file);
            return true;
        } catch (JAXBException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static final String WINDOWS_BASE_DIR = "%APPDATA%/";
    // WORKING
    private static final String OSX_BASE_DIR = "/Library/ApplicationSupport/";
    // NOT TESTED
    private static final String LINUX_BASE_DIR = "/.config/";
    // TESTED ON:
    // POP_OS! - WORKING
    private static final String APPNAME = "Strategist/";

    private static final String EXTENSION = ".xml";

    private static final File findFile(String filename) {
        OSUtils.OS os = OSUtils.getOS();

        String dirPath = null;

        switch (os) {
            case WINDOWS:
                dirPath = WINDOWS_BASE_DIR + APPNAME;
                // The fucking quadruple backslash makes an appearance here because why not
                dirPath.replaceAll("/", "\\\\");
                break;
            case OSX:
                dirPath = OSUtils.getUserHome() + OSX_BASE_DIR + APPNAME;
                break;
            case LINUX:
                dirPath = OSUtils.getUserHome() + LINUX_BASE_DIR + APPNAME.toLowerCase();
                break;
        }

        File dir = new File(dirPath);
        if (!dir.exists())
            dir.mkdirs();

        String path = dirPath + filename + EXTENSION;

        return new File(path);
    }
}