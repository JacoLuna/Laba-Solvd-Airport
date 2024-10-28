package labaSolvd.JacoLuna.Services.entityServices;

import jakarta.xml.bind.JAXBException;
import labaSolvd.JacoLuna.Classes.Passenger;
import labaSolvd.JacoLuna.Classes.Plane;
import labaSolvd.JacoLuna.Classes.xmlLists.Planes;
import labaSolvd.JacoLuna.Connection.SessionFactoryBuilder;
import labaSolvd.JacoLuna.DAO.PlaneDAO;
import labaSolvd.JacoLuna.Enums.JsonPaths;
import labaSolvd.JacoLuna.Enums.SourceOptions;
import labaSolvd.JacoLuna.Enums.XmlPaths;
import labaSolvd.JacoLuna.Interfaces.IService;
import labaSolvd.JacoLuna.Parsers.JAX.Marshaller;
import labaSolvd.JacoLuna.Parsers.JSON.JsonParser;
import labaSolvd.JacoLuna.Services.InputService;
import labaSolvd.JacoLuna.Utils;
import labaSolvd.JacoLuna.myBatysDAO.PlaneMapper;
import org.apache.ibatis.session.SqlSession;

import java.lang.reflect.Field;
import java.util.*;

public class PlaneService implements IService<Plane> {
    private final PlaneDAO planeDAO;
    private final SourceOptions source;
    private Planes planes;
    private List<Plane> planeList;
    private final int MIN_FUEL_CAPACITY = 150000;
    private final int MAX_FUEL_CAPACITY = 300000;

    public PlaneService(SourceOptions sourceOptions) {
        source = sourceOptions;
        if (source == SourceOptions.XML) {
            planes = new Planes();
        }
        this.planeDAO = new PlaneDAO();
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
            case DATA_BASE -> {
                try (SqlSession session = SessionFactoryBuilder.getSqlSessionFactory().openSession()) {
                    return session.getMapper(PlaneMapper.class).getPlane(id);
                } catch (Exception e) {
                    Utils.CONSOLE_ERROR.error(e);
                }
            }
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
            case DATA_BASE -> {
                try (SqlSession session = SessionFactoryBuilder.getSqlSessionFactory().openSession()) {
                    if (session.getMapper(PlaneMapper.class).deletePlane(id) > 0) {
                        session.commit();
                        result = true;
                    }

                } catch (Exception e) {
                    Utils.CONSOLE_ERROR.error(e);
                }
            }
        }
        return result;
    }

    @Override
    public List<Plane> getAll() {
        switch (source) {
            case XML -> {
                try {
                    return Marshaller.UnMarshallList(Planes.class, XmlPaths.PLANES);
                } catch (JAXBException e) {
                    Utils.CONSOLE_ERROR.error(e);
                }
            }
            case JSON -> {
                return JsonParser.unparseToList(Plane.class, JsonPaths.PLANES);
            }
            case DATA_BASE -> {
                try (SqlSession session = SessionFactoryBuilder.getSqlSessionFactory().openSession()) {
                    return session.getMapper(PlaneMapper.class).getAllPlanes();
                } catch (Exception e) {
                    Utils.CONSOLE_ERROR.error(e);
                }
            }
        }
        return null;
    }

    @Override
    public List<Plane> search() {
        List<Plane> searchList = null;
        try (SqlSession session = SessionFactoryBuilder.getSqlSessionFactory().openSession()) {
            List<Field> attributes = Arrays.stream(Plane.class.getDeclaredFields()).toList();
            int attIndex = selectAtt(attributes);
            Object value;

            if (attributes.get(attIndex).getType().equals(String.class)) {
                value = InputService.stringAns("Please enter the search value");
                searchList = session.getMapper(PlaneMapper.class).searchByString(attributes.get(attIndex).getName(), (String) value);
            } else {

                Class<?> fieldType = attributes.get(attIndex).getType();
                Utils.CONSOLE.info(fieldType.getName());
                String prompt = "Please enter the search value";
                value = switch (fieldType.getName()) {
                    case "int" -> InputService.setInput(prompt, Integer.class);
                    case "double" -> InputService.setInput(prompt, Double.class);
                    case "float" -> InputService.setInput(prompt, Float.class);
                    case "boolean" -> InputService.booleanAns("is the value true?");
                    default -> null;
                };

                if (value != null) {
                    if (value.getClass() == Boolean.class)
                        searchList = session.getMapper(PlaneMapper.class).searchByBoolean(attributes.get(attIndex).getName(), (Boolean) value);
                    else
                        searchList = session.getMapper(PlaneMapper.class).searchByNumber(attributes.get(attIndex).getName(), (Number) value);
                }
            }
        } catch (Exception e) {
            Utils.CONSOLE.error("Error while searching passengers: {}", e.getMessage());
        }
        return searchList;
    }

    @Override
    public void update() {
        Plane plane = getById();
        int index = planeList.indexOf(plane);
        updatePlaneAttributes(plane);
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
            case DATA_BASE -> {
                try (SqlSession session = SessionFactoryBuilder.getSqlSessionFactory().openSession()) {
                    if (session.getMapper(PlaneMapper.class).updatePlane(plane) > 0) {
                        session.commit();
                    }
                } catch (Exception e) {
                    Utils.CONSOLE_ERROR.error(e);
                }
            }
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

    private int selectAtt(List<Field> attributes) {
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
            case "fuelCapacity" -> plane.setFuelCapacity(InputService.setInput("Fuel capacity: ", Integer.class));
            case "tripulationSize" ->
                    plane.setTripulationSize(InputService.setInput("Tripulation size: ", Integer.class));
            case "economySize" -> plane.setEconomySize(InputService.setInput("economy size: ", Integer.class));
            case "premiumSize" -> plane.setPremiumSize(InputService.setInput("premium size: ", Integer.class));
            case "businessSize" -> plane.setBusinessSize(InputService.setInput("business size: ", Integer.class));
            case "firstClassSize" -> plane.setFirstClassSize(InputService.setInput("firstClass size: ", Integer.class));
            case "country" -> plane.setCountry(InputService.stringAns("country: "));
        }
    }
}
