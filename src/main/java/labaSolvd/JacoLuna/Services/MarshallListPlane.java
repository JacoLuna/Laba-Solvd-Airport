package labaSolvd.JacoLuna.Services;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import labaSolvd.JacoLuna.Classes.Plane;
import labaSolvd.JacoLuna.Utils;
import labaSolvd.JacoLuna.Classes.xmlLists.Planes;

import java.io.File;
import java.util.List;

public class MarshallListPlane{
    public static void MarshallListPlane(Planes planes) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Planes.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        jaxbMarshaller.marshal(planes, System.out);
        jaxbMarshaller.marshal(planes, new File("src\\main\\java\\labaSolvd\\JacoLuna\\files\\xml\\Planes.xml"));
    }

    public static List<Plane> UnMarshallListPlane() throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Planes.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        Planes planes = (Planes) jaxbUnmarshaller.unmarshal(new File("src\\main\\java\\labaSolvd\\JacoLuna\\files\\xml\\Planes.xml"));
        return planes.getPlanes();
    }
}
