package id.ac.ui.cs.rizzerve.back_end_rizzerve.rating.service.strategy;

import java.util.List;

public class SimpleAverageStrategy implements AverageRatingStrategy {
    @Override
    public double calculateAverage(List<Integer> ratings) {
        if (ratings.isEmpty()) return 0.0;
        return ratings.stream().mapToDouble(Integer::doubleValue).average().orElse(0.0);
    }
}
