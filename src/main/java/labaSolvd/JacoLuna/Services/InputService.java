package labaSolvd.JacoLuna.Services;

import labaSolvd.JacoLuna.Exceptions.MenuException;
import labaSolvd.JacoLuna.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class InputService {
    private static final Logger LOGGER = LogManager.getLogger(Logger.class);
    private static final Logger FILE = LogManager.getLogger("FileOnlyLogger");
    private static final Logger CONSOLE_ERROR = LogManager.getLogger("ConsoleErrorLogger");
    private static final Logger CONSOLE = LogManager.getLogger("ConsoleLogger");
    public static  Scanner keyboard = new Scanner(System.in);

    public static  <T extends Number> T setInput(String prompt, List<T> ansArray, Class<T> type){
        T answer = null;
        boolean isValid = false;
        do {
            System.out.print(prompt);
            try {
                answer = readAnswer(type);
                if (!ansArray.contains(answer)){
                    throw new MenuException("ERROR Option not available");
                }
                isValid = true;
            }catch (InputMismatchException | MenuException e){
                CONSOLE_ERROR.error(e);
                keyboard.nextLine();
            }catch (Exception e){
                LOGGER.error(e);
            }
        } while (!isValid);
        return answer;
    }

    public static <T extends Number> T setInput(String prompt, T minValue, T maxValue, Class<T> type){
        T answer = null;
        boolean isValid = false;
        do {
            CONSOLE.info(prompt);
            try {
                answer = readAnswer(type);
                if (answer.doubleValue() < minValue.doubleValue() || answer.doubleValue() > maxValue.doubleValue()) {
                    throw new MenuException("ERROR Option not available");
                }
                isValid = true;
            }catch (InputMismatchException | MenuException e){
                CONSOLE_ERROR.error(e);
                keyboard.nextLine();
            }catch (Exception e){
                LOGGER.error(e);
            }
        } while (!isValid);
        return answer;
    }
    public static <T extends Number> T setInput(String prompt, Class<T> type){
        T answer = null;
        boolean isValid = false;
        do {
            CONSOLE.info(prompt);
            try {
                answer = readAnswer(type);
                isValid = true;
            }catch (InputMismatchException e){
                CONSOLE_ERROR.error(e);
                keyboard.nextLine();
            }catch (Exception e){
                LOGGER.error(e);
            }
        } while (!isValid);
        return answer;
    }

    public static <T extends Number> T setInput(String prompt, T maxValue, Class<T> type){
        T answer = null;
        boolean isValid = false;
        do {
            CONSOLE.info(prompt);
            try {
                answer = readAnswer(type);
                if (answer.doubleValue() < 0 || answer.doubleValue() > maxValue.doubleValue()) {
                    throw new MenuException("ERROR Option not available");
                }
                isValid = true;
            }catch (InputMismatchException | MenuException e){
                CONSOLE_ERROR.error(e);
                keyboard.nextLine();
            }catch (Exception e){
                LOGGER.error(e);
            }
        } while (!isValid);
        return answer;
    }
    public static <T extends Number> T readAnswer(Class<T> type){
        return switch (type.getSimpleName()) {
            case "Integer" -> type.cast(keyboard.nextInt());
            case "Float" -> type.cast(keyboard.nextFloat());
            case "Long" -> type.cast(keyboard.nextLong());
            case "Double" -> type.cast(keyboard.nextDouble());
            default -> throw new IllegalArgumentException("Unsupported type: " + type);
        };
    }

    public static boolean booleanAns(String prompt) {
        return InputService.setInput(prompt + "\n0 - No\n1 - Yes\n", Arrays.asList(0, 1), Integer.class) == 1;
    }
    public static String stringAns(String prompt){
        Utils.CONSOLE.info(prompt);
        return keyboard.next();
    }
    public static LocalDate readValidDate(String prompt) {
        Utils.CONSOLE.info(prompt);
        return readValidDate();
    }
    public static LocalDate readValidDate() {
        int year, month, day;
        boolean validDate = false;
        LocalDate date = null;

        while (!validDate) {
            try {
                year = setInput("Enter year (" + LocalDate.MIN.getYear() + "-" +  LocalDate.MAX.getYear() + "): ", LocalDate.MIN.getYear(), LocalDate.MAX.getYear(), Integer.class);
                month = setInput("Enter month (1-12): ", 1, 12, Integer.class);
                day = setInput("Enter day (1-31): ", 1, 31, Integer.class);
                date = LocalDate.of(year, month, day);
                validDate = true;
            } catch (DateTimeException | IllegalArgumentException e) {
                Utils.CONSOLE.info("Invalid date. Please try again.");
            }
        }
        return date;
    }
}
