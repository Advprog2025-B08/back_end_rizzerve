package id.ac.ui.cs.rizzerve.back_end_rizzerve.service.strategy;

import java.util.List;

public interface AverageRatingStrategy {
    double calculateAverage(List<Integer> ratings);
}
