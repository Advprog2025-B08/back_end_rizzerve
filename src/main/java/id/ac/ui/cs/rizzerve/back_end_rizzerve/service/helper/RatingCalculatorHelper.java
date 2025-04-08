package id.ac.ui.cs.rizzerve.back_end_rizzerve.service.helper;

public class RatingCalculatorHelper {

    private static RatingCalculatorHelper instance;

    private RatingCalculatorHelper() {}

    public static RatingCalculatorHelper getInstance() {
        if (instance == null) {
            instance = new RatingCalculatorHelper();
        }
        return instance;
    }
}
