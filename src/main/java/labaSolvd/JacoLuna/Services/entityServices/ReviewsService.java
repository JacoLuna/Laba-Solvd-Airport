package labaSolvd.JacoLuna.Services.entityServices;

import jakarta.xml.bind.JAXBException;
import labaSolvd.JacoLuna.Classes.Review;
import labaSolvd.JacoLuna.Classes.xmlLists.Reviews;
import labaSolvd.JacoLuna.Connection.SessionFactoryBuilder;
import labaSolvd.JacoLuna.Enums.JsonPaths;
import labaSolvd.JacoLuna.Enums.SourceOptions;
import labaSolvd.JacoLuna.Enums.XmlPaths;
import labaSolvd.JacoLuna.Interfaces.IService;
import labaSolvd.JacoLuna.Parsers.JAX.Marshaller;
import labaSolvd.JacoLuna.Parsers.JSON.JsonParser;
import labaSolvd.JacoLuna.Services.InputService;
import labaSolvd.JacoLuna.Utils;
import labaSolvd.JacoLuna.myBatysDAO.ReviewMapper;
import org.apache.ibatis.session.SqlSession;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class ReviewsService extends EntityService<Review> implements IService<Review> {

    private final SourceOptions source;
    private Reviews reviews;
    public List<Review> reviewList;
    public ReviewsService(SourceOptions source) {
        super(Review.class);
        this.source = source;
        if (source == SourceOptions.XML){
            reviews = new Reviews();
        }
        reviewList = getAll();
    }
    @Override
    public void add() {
        Review review;
        long idReview = InputService.setInput("idReview",(long)100, Long.class);
        long idFlight = InputService.setInput("IdFlight",(long)100, Long.class);
        long idPassenger = InputService.setInput("IdPassenger",(long)100, Long.class);
        int rating = InputService.setInput("Rating",100, Integer.class);
        String comment = InputService.stringAns("Please enter the comment");
        review = new Review(idReview,idFlight,idPassenger,rating,comment);

        if (source == SourceOptions.DATA_BASE){
            try (SqlSession session = SessionFactoryBuilder.getSqlSessionFactory().openSession()) {
                if (session.getMapper(ReviewMapper.class).insertReview(review) > 0) {
                    session.commit();
                    Utils.CONSOLE.info("Review added code:{}", review.getIdReview());
                }
            } catch (Exception e) {
                Utils.CONSOLE_ERROR.error(e);
            }
        }else {
            reviewList.add(review);
            reviews.setReviews(reviewList);
            try {
                Marshaller.MarshallList(reviews, Reviews.class, XmlPaths.REVIEWS);
            } catch (JAXBException e) {
                Utils.CONSOLE_ERROR.error(e);
            }
        }
    }

    @Override
    public Review getById() {
        long id = super.selectId();
        switch (source) {
            case XML, JSON -> {
                try {
                    if (source == SourceOptions.XML)
                        reviewList = Marshaller.UnMarshallList(Review.class, XmlPaths.REVIEWS);
                    else
                        reviewList = JsonParser.unparseToList(Review.class, JsonPaths.REVIEWS);

                    if (reviewList != null && !reviewList.isEmpty()) {
                        return reviewList.stream()
                                .filter(p -> p.getIdReview() == id)
                                .findFirst()
                                .orElse(null);
                    }
                } catch (JAXBException e) {
                    Utils.CONSOLE_ERROR.error(e);
                }
            }
            case DATA_BASE -> {
                try (SqlSession session = SessionFactoryBuilder.getSqlSessionFactory().openSession()) {
                    return session.getMapper(ReviewMapper.class).getReview(id);
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
        long id = super.selectId();
        switch (source) {
            case XML, JSON -> {
                try {
                    if (source == SourceOptions.XML)
                        reviewList = Marshaller.UnMarshallList(Review.class, XmlPaths.REVIEWS);
                    else
                        reviewList = JsonParser.unparseToList(Review.class, JsonPaths.REVIEWS);

                    if (reviewList != null && !reviewList.isEmpty()) {
                        reviewList.remove(reviewList.stream()
                                .filter(p -> p.getIdReview() == id)
                                .findFirst()
                                .orElse(null)
                        );
                    }
                    if (source == SourceOptions.XML) {
                        this.reviews.setReviews(reviewList);
                        Marshaller.MarshallList(this.reviews, Reviews.class, XmlPaths.REVIEWS);
                    } else
                        JsonParser.parse(reviewList, JsonPaths.REVIEWS);
                    result = true;
                } catch (JAXBException e) {
                    Utils.CONSOLE_ERROR.error(e);
                }
            }
            case DATA_BASE -> {
                try (SqlSession session = SessionFactoryBuilder.getSqlSessionFactory().openSession()) {
                    if (session.getMapper(ReviewMapper.class).deleteReview(id) > 0) {
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
    public List<Review> getAll() {
        switch (source) {
            case XML -> {
                try {
                    return Marshaller.UnMarshallList(Reviews.class, XmlPaths.REVIEWS);
                } catch (JAXBException e) {
                    Utils.CONSOLE_ERROR.error(e);
                }
            }
            case JSON -> {
                return JsonParser.unparseToList(Review.class, JsonPaths.REVIEWS);
            }
            case DATA_BASE -> {
                try (SqlSession session = SessionFactoryBuilder.getSqlSessionFactory().openSession()) {
                    return session.getMapper(ReviewMapper.class).getAllReviews();
                } catch (Exception e) {
                    Utils.CONSOLE_ERROR.error(e);
                }
            }
        }
        return null;
    }

    @Override
    public List<Review> search() {
        List<Review> searchList = null;
        try (SqlSession session = SessionFactoryBuilder.getSqlSessionFactory().openSession()) {
            List<Field> attributes = Arrays.stream(Review.class.getDeclaredFields()).toList();
            int attIndex = selectAtt(attributes);
            Field attribute = attributes.get(attIndex);
            Object value;

            if (attribute.getType().equals(String.class)) {
                value = InputService.stringAns("Please enter the search value");
                searchList = session.getMapper(ReviewMapper.class).searchByString(attribute.getName(), (String) value);
            } else {
                Class<?> fieldType = attribute.getType();
                Utils.CONSOLE.info(fieldType.getName());
                String prompt = "Please enter the search value";
                value = InputService.setInput(prompt, Integer.class);

                if (value != null) {
                    searchList = session.getMapper(ReviewMapper.class).searchByNumber(attribute.getName(), (Number) value);
                }
            }
        } catch (Exception e) {
            Utils.CONSOLE.error("Error while searching reviews: {}", e.getMessage());
        }
        return searchList;
    }

    @Override
    public void update() {
        Review review = getById();
        int index = reviewList.indexOf(review);
        super.updateEntityAttributes(review);
        reviewList.set(index, review);
        switch (source) {
            case XML -> {
                reviews.setReviews(reviewList);
                try {
                    Marshaller.MarshallList(reviews, Reviews.class, XmlPaths.REVIEWS);
                } catch (JAXBException e) {
                    Utils.CONSOLE_ERROR.error(e);
                }
            }
            case JSON -> JsonParser.parse(reviewList, JsonPaths.REVIEWS);
            case DATA_BASE -> {
                try (SqlSession session = SessionFactoryBuilder.getSqlSessionFactory().openSession()) {
                    if (session.getMapper(ReviewMapper.class).updateReview(review) > 0) {
                        session.commit();
                    }
                } catch (Exception e) {
                    Utils.CONSOLE_ERROR.error(e);
                }
            }
        }
    }
}
