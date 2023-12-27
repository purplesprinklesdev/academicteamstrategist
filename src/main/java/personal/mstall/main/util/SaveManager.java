package personal.mstall.main.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import static java.nio.file.StandardCopyOption.*;

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

    public static boolean exportDataTo(File destDir) {
        String destDirPath = destDir.getAbsolutePath() + File.separator;
        if (OSUtils.getOS() == OSUtils.OS.LINUX)
            destDirPath += APPNAME.toLowerCase();
        else
            destDirPath += APPNAME;

        return copySaveFile(new File(baseDirPath()), new File(destDirPath));
    }
    public static boolean importDataFrom(File sourceDir) {
        return copySaveFile(sourceDir, new File(baseDirPath()));
    }

    private static final String APPNAME = "Strategist/";

    private static final String EXTENSION = ".xml";

    private static final boolean copySaveFile(File sourceDir, File targetDir) {
        File sRoster = new File(sourceDir.getAbsolutePath() + File.separator + "roster.xml");
        File sScoreSheets = new File(sourceDir.getAbsolutePath() + File.separator + "scoresheets.xml");
        File sTeamComps = new File(sourceDir.getAbsolutePath() + File.separator + "teamcomp.xml");

        File tRoster = new File(targetDir.getAbsolutePath() + File.separator + "roster.xml");
        File tScoreSheets = new File(targetDir.getAbsolutePath() + File.separator + "scoresheets.xml");
        File tTeamComps = new File(targetDir.getAbsolutePath() + File.separator + "teamcomp.xml");
        
        try {
            if (!targetDir.exists())
                targetDir.mkdirs();

            Files.copy(sRoster.toPath(), tRoster.toPath(), COPY_ATTRIBUTES, REPLACE_EXISTING);
            Files.copy(sScoreSheets.toPath(), tScoreSheets.toPath(), COPY_ATTRIBUTES, REPLACE_EXISTING);
            Files.copy(sTeamComps.toPath(), tTeamComps.toPath(), COPY_ATTRIBUTES, REPLACE_EXISTING);
            
            return true;
        } catch(IOException e) {
            e.printStackTrace();
            return false;
        }
        
    }

    private static final File findFile(String filename) {
        File dir = new File(baseDirPath());
        if (!dir.exists())
            dir.mkdirs();

        String path = baseDirPath() + filename + EXTENSION;

        return new File(path);
    }

    private static final String baseDirPath() {
        OSUtils.OS os = OSUtils.getOS();

        String dirPath = OSUtils.baseDir();

        dirPath += APPNAME;

        if (os == OSUtils.OS.LINUX)
            dirPath = dirPath.toLowerCase();

        return dirPath;
    }
}