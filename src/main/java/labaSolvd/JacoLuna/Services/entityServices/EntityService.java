package labaSolvd.JacoLuna.Services.entityServices;

import labaSolvd.JacoLuna.Classes.Plane;
import labaSolvd.JacoLuna.Interfaces.IService;
import labaSolvd.JacoLuna.Services.InputService;
import labaSolvd.JacoLuna.Utils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public abstract class EntityService<T> implements IService<T> {

    private final Class<T> type;

    public EntityService(Class<T> type) {
        this.type = type;
    }

    protected long selectId() {
        List<T> entity = getAll();
        StringBuilder sb = new StringBuilder("Select the id");
        List<Long> ids = new ArrayList<>();
        entity.forEach(e -> {
            try {
                Method getId = type.getMethod("getId" + type.getSimpleName());
                ids.add((Long) getId.invoke(e));
                sb.append("\n").append(getId.invoke(e));
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException ex) {
                Utils.CONSOLE_ERROR.error("an error occurred while invoking the method {}", ex.getMessage());
            }
        });
        sb.append("\n");
        return InputService.setInput(sb.toString(), ids, Long.class);
    }

//    protected String selectId() {
//        StringBuilder sb = new StringBuilder("Select the code of the plane");
//        List<String> codeList = new ArrayList<>();
//        String code;
//        for (Plane p : planeList) {
//            codeList.add(p.getIdPlane());
//            sb.append("\n").append(p.getIdPlane());
//        }
//        sb.append("\n");
//        code = InputService.stringAns(sb.toString(), codeList);
//        return code;
//    }

    protected void updateEntityAttributes(T entity) {
        List<Field> attributes = getEntityAttributes();
        int ans;
        do {
            ans = selectAtt(attributes);
            System.out.println(ans);
            if (ans < attributes.size())
                updateAttribute(entity, attributes.get(ans), attributes);
        } while (ans != attributes.size());
    }

    protected int selectAtt(List<Field> attributes) {
        int ans;
        StringBuilder sb = new StringBuilder("Select an attribute\n");
        for (int i = 0; i < attributes.size(); i++) {
            sb.append("\n").append(i).append(" ").append(attributes.get(i).getName());
        }
        sb.append("\n").append(attributes.size()).append(" Done\n");
        ans = InputService.setInput(sb.toString(), attributes.size(), Integer.class);
        return ans;
    }

    protected List<Field> getEntityAttributes() {
        LinkedList<Field> superAttributes = new LinkedList<>();
        if (type.getSuperclass() != null) {
            superAttributes = new LinkedList<>(Arrays.asList(type.getSuperclass().getDeclaredFields()));
        }
        LinkedList<Field> childAttributes = new LinkedList<>(Arrays.asList(type.getDeclaredFields()));
        superAttributes.addAll(childAttributes);
        superAttributes.removeIf(f -> f.getName().contains("id"));

        return superAttributes;
    }

    protected void updateAttribute(T entity, Field field, List<Field> fields) {
        String prompt = "Input value: ";
        for (Field f : fields) {
            if (field.getName().equalsIgnoreCase(f.getName())) {
                try {
                    f.setAccessible(true);
                    Method method =
                            type.getMethod("set" + StringUtils.capitalize(f.getName()), field.getType());
                    switch (field.getType().getSimpleName()) {
                        case "int" -> method.invoke(entity, InputService.setInput(prompt, Integer.class));
                        case "float" -> method.invoke(entity, InputService.setInput(prompt, Float.class));
                        case "long" -> method.invoke(entity, InputService.setInput(prompt, Long.class));
                        case "double" -> method.invoke(entity, InputService.setInput(prompt, Double.class));
                        case "boolean" -> method.invoke(entity, InputService.booleanAns("Is this value true?"));
                        case "String" -> method.invoke(entity, InputService.stringAns(prompt));
                        default -> throw new IllegalArgumentException("Unsupported type: " + type);
                    }
                } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                    Utils.CONSOLE_ERROR.error("an error occurred while invoking the method {}", e.getMessage());
                }
            }
        }
    }
}
