package labaSolvd.JacoLuna.Services;

import labaSolvd.JacoLuna.Utils;

import static com.diogonunes.jcolor.Attribute.BLACK_TEXT;
import static com.diogonunes.jcolor.Attribute.BRIGHT_MAGENTA_BACK;
import static com.diogonunes.jcolor.Ansi.colorize;

public class MenuService {
    public static void printFrame(String word, int frame) {
        int middleSection = frame - word.length() - 1;
        printLine(frame);
        printMiddleSection(word, middleSection);
        printLine(frame);
    }
    public static void printFrame(String word) {
        printFrame(word, 30);
    }
    private static void printLine(int frame) {
        for (int i = 0; i < frame; i++) {
            System.out.print("-" + ((i == frame - 1) ? "\n" : ""));
        }
    }
    private static void printMiddleSection(String word, int middleSection) {
        for (int i = 0; i < middleSection; i++) {
            if (i == (middleSection / 2)) {
                System.out.print(colorize(word , BLACK_TEXT(), BRIGHT_MAGENTA_BACK()));
            } else {
                if (i == middleSection - 1 || i == 0) {
                    if (i == middleSection - 1){
                        for (int j = 0; j < 3; j++) {
                            if (j != 2)
                                System.out.print(colorize(" " , BLACK_TEXT(), BRIGHT_MAGENTA_BACK()));
                            else
                                System.out.print(("|\n"));
                        }
                    }else {
                        System.out.print(("|"));
                    }
                } else {
                    System.out.print(colorize(" " , BLACK_TEXT(), BRIGHT_MAGENTA_BACK()));
                }
            }
        }
    }
    public static void printMenu(String title, String[] ans) {
        printFrame(title);
        Utils.CONSOLE.info(buildMenuString(ans));
    }
    public static void printMenu(String title, String[] ans, int frame) {
        printFrame(title, frame);
        Utils.CONSOLE.info(buildMenuString(ans));
    }
    public static void printMenu(String[] ans) {
        Utils.CONSOLE.info(buildMenuString(ans));
    }
    private static String buildMenuString(String[] ans) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ans.length; i++) {
            sb.append("-").append(i).append(" ").append(ans[i]).append("\n");
        }
        return sb.toString();
    }
}
