package labaSolvd.JacoLuna.Services.entityServices;

import jakarta.xml.bind.JAXBException;
import labaSolvd.JacoLuna.Classes.Review;
import labaSolvd.JacoLuna.Classes.xmlLists.Reviews;
import labaSolvd.JacoLuna.Enums.SourceOptions;
import labaSolvd.JacoLuna.Enums.XmlPaths;
import labaSolvd.JacoLuna.Interfaces.IService;
import labaSolvd.JacoLuna.Parsers.JAX.Marshaller;
import labaSolvd.JacoLuna.Services.InputService;
import labaSolvd.JacoLuna.Utils;

import java.util.List;

public class ReviewsService implements IService<Review> {

    private final SourceOptions source;
    private Reviews reviews;
    private Marshaller<Reviews, Review> marshaller;
    public ReviewsService(SourceOptions source) {
        this.source = source;
        if (source == SourceOptions.XML){
            reviews = new Reviews();
            marshaller = new Marshaller<>();
        }
    }
    @Override
    public void add() {
        Review Review;
        long idReview = InputService.setInput("idReview",(long)100, Long.class);
        long idFlight = InputService.setInput("IdFlight",(long)100, Long.class);
        long idPassenger = InputService.setInput("IdPassenger",(long)100, Long.class);
        int rating = InputService.setInput("Rating",100, Integer.class);
        String comment = InputService.stringAns("Please enter the comment");
        Review = new Review(idReview,idFlight,idPassenger,rating,comment);
        if (source == SourceOptions.DATA_BASE){
           /* if (ReviewDAO.add(Review) != -1){
                Utils.CONSOLE.info("Review added code:{}", ReviewCode);
            }*/
        }else {
            List<Review> newList = getAll();
            newList.add(Review);
            reviews.setReviews(newList);
            try {
                marshaller.MarshallList(reviews, Reviews.class, XmlPaths.REVIEWS);
            } catch (JAXBException e) {
                Utils.CONSOLE_ERROR.error(e);
            }
        }
    }

    @Override
    public Review getById() {
        return null;
    }

    @Override
    public boolean delete() {
        return false;
    }

    @Override
    public List<Review> getAll() {
        if (source == SourceOptions.DATA_BASE)
//            return reviewDAO.getList();
            return null;
        try {
            return marshaller.UnMarshallList(Reviews.class, XmlPaths.REVIEWS);
        } catch (JAXBException e) {
            Utils.CONSOLE_ERROR.error(e);
        }
        return null;
    }

    @Override
    public List<Review> search() {
        return List.of();
    }

    @Override
    public void update() {

    }
}
