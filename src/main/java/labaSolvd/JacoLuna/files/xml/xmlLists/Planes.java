package labaSolvd.JacoLuna.files.xml.xmlLists;

import jakarta.xml.bind.annotation.*;
import labaSolvd.JacoLuna.Classes.Plane;

import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "Planes")
@XmlAccessorType(XmlAccessType.FIELD)
public class Planes {
    @XmlElement(name = "Planes")
    private List<Plane> planes = null;
    public Planes() {
        planes = new ArrayList<Plane>();
    }
    public List<Plane> getPlanes() {
        return planes;
    }

    public void setPlanes(List<Plane> planes) {
        this.planes = planes;
    }
}
