package labaSolvd.JacoLuna.Services;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import jdk.jshell.execution.Util;
import labaSolvd.JacoLuna.Classes.Plane;
import labaSolvd.JacoLuna.Utils;
import labaSolvd.JacoLuna.files.xml.xmlLists.Planes;

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

    public static void UnMarshallListPlane() throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Planes.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        //We had written this file in marshalling example
        Planes planes = (Planes) jaxbUnmarshaller.unmarshal(new File("src\\main\\java\\labaSolvd\\JacoLuna\\files\\xml\\Planes.xml"));

        for (Plane plane : planes.getPlanes()) {
            Utils.CONSOLE.info(plane);
        }
    }
}
