package labaSolvd.JacoLuna.Services.entityServices;

import jakarta.xml.bind.JAXBException;
import labaSolvd.JacoLuna.Classes.Passenger;
import labaSolvd.JacoLuna.Classes.Plane;
import labaSolvd.JacoLuna.Classes.xmlLists.Planes;
import labaSolvd.JacoLuna.Connection.SessionFactoryBuilder;
import labaSolvd.JacoLuna.DAO.EntityDAO;
import labaSolvd.JacoLuna.DAO.PlaneDAO;
import labaSolvd.JacoLuna.Enums.JsonPaths;
import labaSolvd.JacoLuna.Enums.SourceOptions;
import labaSolvd.JacoLuna.Enums.XmlPaths;
import labaSolvd.JacoLuna.Interfaces.IService;
import labaSolvd.JacoLuna.Parsers.JAX.Marshaller;
import labaSolvd.JacoLuna.Parsers.JSON.JsonParser;
import labaSolvd.JacoLuna.Services.InputService;
import labaSolvd.JacoLuna.Utils;
import labaSolvd.JacoLuna.myBatysDAO.CrewMemberMapper;
import labaSolvd.JacoLuna.myBatysDAO.PlaneMapper;
import org.apache.ibatis.session.SqlSession;

import java.lang.reflect.Field;
import java.util.*;

public class PlaneService extends EntityService<Plane> implements IService<Plane> {
    private final SourceOptions source;
    private Planes planes;
    private List<Plane> planeList;
    private final int MIN_FUEL_CAPACITY = 150000;
    private final int MAX_FUEL_CAPACITY = 300000;

    public PlaneService(SourceOptions sourceOptions) {
        super(Plane.class);
        source = sourceOptions;
        if (source == SourceOptions.XML) {
            planes = new Planes();
        }
        planeList = getAll();
    }

    @Override
    public void add() {
        Plane plane = inputData();
        planeList.add(plane);
        switch (source) {
            case XML -> {
                planes.setPlanes(planeList);
                try {
                    Marshaller.MarshallList(planes, Planes.class, XmlPaths.PLANES);
                } catch (JAXBException e) {
                    Utils.CONSOLE_ERROR.error(e);
                }
            }
            case JSON -> JsonParser.parse(planeList, JsonPaths.PLANES);
            case DATA_BASE -> {
                try (SqlSession session = SessionFactoryBuilder.getSqlSessionFactory().openSession()) {
                    if (session.getMapper(PlaneMapper.class).insertPlane(plane) > 0) {
                        session.commit();
                        Utils.CONSOLE.info("Plane added code:{}", plane.getIdPlane());
                    }
                } catch (Exception e) {
                    Utils.CONSOLE_ERROR.error(e);
                }
            }
        }
    }

    private Plane inputData() {
        String planeCode = enterPlaneCode();
        int fuelCapacity = InputService.setInput("Fuel capacity", MIN_FUEL_CAPACITY, MAX_FUEL_CAPACITY, Integer.class);
        HashMap<String, Integer> planeCapacity = setPlaneCapacity(planeCode);
        String country = InputService.stringAns("Please enter the country");
        return new Plane(planeCode, fuelCapacity,
                planeCapacity.get("tripulationSize"), planeCapacity.get("economySize"),
                planeCapacity.get("premiumSize"), planeCapacity.get("businessSize"),
                planeCapacity.get("firstClassSize"), country);

    }

    private HashMap<String, Integer> setPlaneCapacity(String planeCode) {
        HashMap<String, Integer> planeCapacity = new HashMap<>();
        int size;
        planeCapacity.put("TripulationSize", InputService.setInput("Tripulation size", 4, 8, Integer.class));
        size = InputService.setInput("How many people can the " + planeCode + " carry?", 50, 300, Integer.class);
        planeCapacity.put("economySize", InputService.setInput("economy size ", 0, size, Integer.class));
        size -= planeCapacity.get("economySize");
        if (size > 0) {
            planeCapacity.put("premiumSize", InputService.setInput("Premium size ", 0, size, Integer.class));
            size -= planeCapacity.get("premiumSize");
            if (size > 0) {
                planeCapacity.put("businessSize", InputService.setInput("Business size ", 0, size, Integer.class));
                planeCapacity.put("firstClassSize", size - planeCapacity.get("businessSize"));
            }
        }
        return planeCapacity;
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
        String id = selectPlaneId();
        switch (source) {
            case XML, JSON -> {
                try {
                    if (source == SourceOptions.XML)
                        planeList = Marshaller.UnMarshallList(Planes.class, XmlPaths.PLANES);
                    else
                        planeList = JsonParser.unparseToList(Plane.class, JsonPaths.PLANES);

                    if (planeList != null && !planeList.isEmpty()) {
                        return planeList.stream()
                                .filter(p -> p.getIdPlane().equals(id))
                                .findFirst()
                                .orElse(null);
                    }
                } catch (JAXBException e) {
                    Utils.CONSOLE_ERROR.error(e);
                }
            }
            case DATA_BASE -> EntityDAO.executeQuery(PlaneMapper.class, "getPlane", id);
        }
        return null;
    }

    @Override
    public boolean delete() {
        boolean result = false;
        String id = selectPlaneId();
        switch (source) {
            case XML, JSON -> {
                try {
                    if (source == SourceOptions.XML)
                        planeList = Marshaller.UnMarshallList(Planes.class, XmlPaths.PLANES);
                    else
                        planeList = JsonParser.unparseToList(Plane.class, JsonPaths.PLANES);

                    if (planeList != null && !planeList.isEmpty()) {
                        planeList.remove(planeList.stream()
                                .filter(p -> p.getIdPlane().equals(id))
                                .findFirst()
                                .orElse(null)
                        );
                    }
                    if (source == SourceOptions.XML) {
                        this.planes.setPlanes(planeList);
                        Marshaller.MarshallList(this.planes, Planes.class, XmlPaths.PLANES);
                    } else
                        JsonParser.parse(planeList, JsonPaths.PLANES);
                } catch (JAXBException e) {
                    Utils.CONSOLE_ERROR.error(e);
                }
            }
            case DATA_BASE -> EntityDAO.executeQuery(PlaneMapper.class, "deletePlane", id);
        }
        return result;
    }

    @Override
    public List<Plane> getAll() {
        return switch (source) {
            case XML -> {
                try {
                    yield Marshaller.UnMarshallList(Planes.class, XmlPaths.PLANES);
                } catch (JAXBException e) {
                    Utils.CONSOLE_ERROR.error(e);
                    yield null;
                }
            }
            case JSON -> JsonParser.unparseToList(Plane.class, JsonPaths.PLANES);
            case DATA_BASE -> EntityDAO.executeQuery(PlaneMapper.class, "getAllPlanes");
            default -> null;
        };
    }

    @Override
    public List<Plane> search() {
        List<Plane> searchList = null;
        List<Field> attributes = Arrays.stream(Plane.class.getDeclaredFields()).toList();
        int attIndex = selectAtt(attributes);
        Field att = attributes.get(attIndex);
        Object value;

        if (att.getType().equals(String.class)) {
            value = InputService.stringAns("Please enter the search value");
        } else {
            Class<?> fieldType = att.getType();
            String prompt = "Please enter the search value";
            value = switch (fieldType.getName()) {
                case "int" -> InputService.setInput(prompt, Integer.class);
                case "double" -> InputService.setInput(prompt, Double.class);
                case "float" -> InputService.setInput(prompt, Float.class);
                case "boolean" -> InputService.booleanAns("is the value true?");
                default -> null;
            };
        }
        if (value != null) {
            switch (source) {
                case XML -> searchList = planeList;
                case JSON -> searchList = planeList;
                case DATA_BASE ->
                        searchList = EntityDAO.executeQuery(PlaneMapper.class, (att.getType().equals(String.class)) ? "searchByString" : "searchByOther", att.getName(), value);
            }
        }

        return searchList;
    }

    @Override
    public void update() {
        Plane plane = getById();
        int index = planeList.indexOf(plane);
        super.updateEntityAttributes(plane);
        planeList.set(index, plane);
        switch (source) {
            case XML -> {
                planes.setPlanes(planeList);
                try {
                    Marshaller.MarshallList(planes, Planes.class, XmlPaths.PLANES);
                } catch (JAXBException e) {
                    Utils.CONSOLE_ERROR.error(e);
                }
            }
            case JSON -> JsonParser.parse(planeList, JsonPaths.PLANES);
            case DATA_BASE -> EntityDAO.executeQuery(PlaneMapper.class, "updatePlane", plane);
        }
    }

    private String selectPlaneId() {
        StringBuilder sb = new StringBuilder("Select the code of the plane");
        List<String> codeList = new ArrayList<>();
        String code;
        for (Plane p : planeList) {
            codeList.add(p.getIdPlane());
            sb.append("\n").append(p.getIdPlane());
        }
        sb.append("\n");
        code = InputService.stringAns(sb.toString(), codeList);
        return code;
    }
}
