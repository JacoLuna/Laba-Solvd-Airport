package labaSolvd.JacoLuna.Services.entityServices;

import jakarta.xml.bind.JAXBException;
import labaSolvd.JacoLuna.Classes.Passenger;
import labaSolvd.JacoLuna.Classes.Plane;
import labaSolvd.JacoLuna.Classes.xmlLists.Planes;
import labaSolvd.JacoLuna.DAO.PlaneDAO;
import labaSolvd.JacoLuna.Enums.SourceOptions;
import labaSolvd.JacoLuna.Enums.XmlPaths;
import labaSolvd.JacoLuna.Interfaces.IService;
import labaSolvd.JacoLuna.Parsers.JAX.Marshaller;
import labaSolvd.JacoLuna.Services.InputService;
import labaSolvd.JacoLuna.Utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class PlaneService implements IService<Plane> {
    private final PlaneDAO planeDAO;
    private final SourceOptions source;
    private Planes planes;
    private Marshaller<Planes, Plane> marshaller;

    public PlaneService(SourceOptions sourceOptions) {
        source = sourceOptions;
        if (source == SourceOptions.XML) {
            planes = new Planes();
            marshaller = new Marshaller<>();
        }
        this.planeDAO = new PlaneDAO();
    }

    @Override
    public void add() {
        Plane plane;
        String planeCode = enterPlaneCode();
        int fuelCapacity = InputService.setInput("Fuel capacity", 150000, 300000, Integer.class);
        int size, tripulationSize, economySize, premiumSize = 0, businessSize = 0, firstClassSize = 0;
        tripulationSize = InputService.setInput("Tripulation size", 4, 8, Integer.class);
        size = InputService.setInput("How many people can the " + planeCode + " carry?", 50, 300, Integer.class);
        economySize = InputService.setInput("economy size ", 0, size, Integer.class);
        size -= economySize;
        if (size > 0) {
            premiumSize = InputService.setInput("Premium size ", 0, size, Integer.class);
            size -= premiumSize;
            if (size > 0) {
                businessSize = InputService.setInput("Business size ", 0, size, Integer.class);
                firstClassSize = size - businessSize;
            }
        }
        String country = InputService.stringAns("Please enter the country");
        plane = new Plane(planeCode, fuelCapacity, tripulationSize, economySize, premiumSize, businessSize, firstClassSize, country);
        if (source == SourceOptions.DATA_BASE) {
            if (planeDAO.add(plane) != -1) {
                Utils.CONSOLE.info("Plane added code:{}", planeCode);
            }
        } else {
            List<Plane> newList = getAll();
            newList.add(plane);
            planes.setPlanes(newList);
            try {
                marshaller.MarshallList(planes, Planes.class, XmlPaths.PLANES);
            } catch (JAXBException e) {
                Utils.CONSOLE_ERROR.error(e);
            }
        }
    }

    private String enterPlaneCode() {
        String planeCode;
        boolean codeExists;
        do {
            planeCode = InputService.stringAns("Please enter plane code\nExample \"A-123\"", "^[A-Z]\\d{3}$");
            String finalPlaneCode = planeCode;
            codeExists = getAll().stream().anyMatch(p -> Objects.equals(p.getIdPlane(), finalPlaneCode));
            if (codeExists) {
                Utils.CONSOLE.info("Code {} already exists", finalPlaneCode);
            }
        } while (codeExists);
        return planeCode;
    }

    @Override
    public Plane getById() {
        if (source == SourceOptions.DATA_BASE) {
            return planeDAO.getById(selectPlaneId());
        } else {

        }
        return null;
    }

    @Override
    public boolean delete() {
        boolean result = false;
        if (source == SourceOptions.DATA_BASE) {
            result = planeDAO.delete(selectPlaneId());
        }else {

        }
        return result;
    }

    @Override
    public List<Plane> getAll() {
        if (source == SourceOptions.DATA_BASE)
            return planeDAO.getList();
        try {
            return marshaller.UnMarshallList(Planes.class, XmlPaths.PLANES);
        } catch (JAXBException e) {
            Utils.CONSOLE_ERROR.error(e);
        }
        return null;
    }

    @Override
    public List<Plane> search() {
        return List.of();
    }

    @Override
    public void update() {
        Plane plane = getById();

    }

    private String selectPlaneId() {
        List<Plane> planes = getAll();
        StringBuilder sb = new StringBuilder("Select the code of the plane");
        List<String> codes = new ArrayList<>();
        List<Integer> indexes = new ArrayList<>();
        int i = 0, index;
        for (Plane p : planes) {
            indexes.add(i);
            i++;
            codes.add(p.getIdPlane());
            sb.append("\n").append(p.getIdPlane());
        }
        sb.append("\n");
        index = InputService.setInput(sb.toString(), indexes, Integer.class);
        return codes.get(index);
    }

    private void updatePlaneAttributes(Plane plane) {
        List<Field> attributes = Arrays.stream(Plane.class.getDeclaredFields()).toList();
        int ans;
        do {
            ans = selectAtt(attributes);
            if (ans < attributes.size()) {
                updateAttribute(plane, attributes.get(ans));
            }
        } while (ans != attributes.size());
    }

    private int selectAtt(List<Field> attributes){
        int ans;
        StringBuilder sb = new StringBuilder("Select an attribute\n");
        for (int i = 1; i < attributes.size(); i++) {
            sb.append("\n").append(i).append(" ").append(attributes.get(i).getName());
        }
        sb.append("\n").append(attributes.size()).append(" none\n");
        ans = InputService.setInput(sb.toString(), attributes.size(), Integer.class);
        return ans;
    }

    private void updateAttribute(Plane plane, Field field) {
        field.setAccessible(true);
        switch (field.getName()) {
            case "fuelCapacity"-> plane.setFuelCapacity(InputService.setInput("Fuel capacity: ", Integer.class));
            case "tripulationSize"-> plane.setTripulationSize(InputService.setInput("Tripulation size: ", Integer.class));
            case "economySize"-> plane.setEconomySize(InputService.setInput("economy size: ", Integer.class));
            case "premiumSize"-> plane.setPremiumSize(InputService.setInput("premium size: ", Integer.class));
            case "businessSize"-> plane.setBusinessSize(InputService.setInput("business size: ", Integer.class));
            case "firstClassSize"-> plane.setFirstClassSize(InputService.setInput("firstClass size: ", Integer.class));
            case "country"-> plane.setCountry(InputService.stringAns("country: "));
        }
    }
}
