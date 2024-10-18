package labaSolvd.JacoLuna;
import labaSolvd.JacoLuna.Services.AirportService;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

/*        boolean isValid = XMLValidator.validateXML(
                "src\\main\\java\\labaSolvd\\JacoLuna\\files\\xml\\Planes.xml",
                "src\\main\\java\\labaSolvd\\JacoLuna\\files\\xml\\xsd\\PlaneSchema.xml");

        if (isValid)
            Utils.CONSOLE.info("is valid");
        else
            Utils.CONSOLE.info("is not valid");*/

        /*SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        try {
            SAXParser saxParser = saxParserFactory.newSAXParser();
            ReviewSaxParser handler = new ReviewSaxParser();
            saxParser.parse(new File("src\\main\\java\\labaSolvd\\JacoLuna\\files\\xml\\Reviews.xml"), handler);
            List<Review> empList = handler.getEmpList();
            for(Review emp : empList)
                System.out.println(emp);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }*/

        AirportService airportService = new AirportService();
        airportService.startProgram();
    }
}