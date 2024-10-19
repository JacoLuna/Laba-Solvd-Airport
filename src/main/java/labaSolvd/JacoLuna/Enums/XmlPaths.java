package labaSolvd.JacoLuna.Enums;

public enum XmlPaths {
    PLANES("Planes.xml"),
    REVIEWS("Reviews.xml");

    public String path = "src\\main\\resources\\xml\\";

    XmlPaths(String s) {
        StringBuilder sb = new StringBuilder(path);
        sb.append(s);
        path = sb.toString();
    }
}
