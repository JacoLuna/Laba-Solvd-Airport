package labaSolvd.JacoLuna.Parsers.JAX;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.XmlRootElement;
import labaSolvd.JacoLuna.Classes.Plane;
import labaSolvd.JacoLuna.Classes.xmlLists.Planes;
import labaSolvd.JacoLuna.Enums.XmlPaths;
import labaSolvd.JacoLuna.Utils;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

public class Marshaller {
    public static <T> void MarshallList(T list, Class<T> type, XmlPaths xmlPath) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(type);
        jakarta.xml.bind.Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty(jakarta.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, true);
        jaxbMarshaller.marshal(list, new File(xmlPath.path));
    }
    public static <T,K> List<K> UnMarshallList(Class<T> type, XmlPaths xmlPath) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(type);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        T listClass = (T) jaxbUnmarshaller.unmarshal(new File(xmlPath.path));
        String xmlName = getXmlRootElementName(type);
        try {
            return (List<K>) type.getMethod("get"+xmlName ).invoke(listClass);
        } catch (InvocationTargetException | IllegalAccessException |
                 NoSuchMethodException | IllegalArgumentException e) {
            Utils.CONSOLE_ERROR.error("Something went wrong unmarshalling the xml file.",e);
        }
        return List.of();
    }
    private static <T> String getXmlRootElementName(Class<T> clazz) {
        if (clazz.isAnnotationPresent(XmlRootElement.class)) {
            XmlRootElement xmlRootElement = clazz.getAnnotation(XmlRootElement.class);
            if (!xmlRootElement.name().equals("##default")) {
                return xmlRootElement.name();
            } else {
                return clazz.getSimpleName();
            }
        }
        throw new IllegalArgumentException("Class " + clazz.getName() + " is not annotated with @XmlRootElement");
    }
}
