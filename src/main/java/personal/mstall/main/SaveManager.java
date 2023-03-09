package personal.mstall.main;
import java.io.File;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

public class SaveManager {
    private static final String PATH = "{0}.xml";

    public static final Object Load(FileType type) {
        File file = new File(String.format(PATH, type.filename));
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
        File file = new File(String.format(PATH, type.filename));
        
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
}
