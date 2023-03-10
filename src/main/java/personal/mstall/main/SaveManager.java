package personal.mstall.main;

import java.io.File;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

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
    // TODO: NOT TESTED
    private static final String OSX_BASE_DIR = "/Library/ApplicationSupport/";
    // TODO: NOT TESTED
    private static final String LINUX_BASE_DIR = "/.config/";
    // TODO: TESTED ON:
    // POP_OS!
    private static final String APPNAME = "Strategist/";
    // TODO: WORKING TITLE
    // Other ideas:
    // The Owl Parliament
    //

    private static final String EXTENSION = ".xml";

    private static final File findFile(String filename) {
        OSUtils.OS os = OSUtils.getOS();

        String dirPath = null;

        switch (os) {
            case WINDOWS:
                dirPath = WINDOWS_BASE_DIR + APPNAME;
                dirPath.replaceAll("/", "\\");
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