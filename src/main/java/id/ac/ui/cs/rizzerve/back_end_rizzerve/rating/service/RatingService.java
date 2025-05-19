package id.ac.ui.cs.rizzerve.back_end_rizzerve.rating.service;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.rating.model.Rating;
import org.springframework.scheduling.annotation.Async;

import java.util.concurrent.CompletableFuture;

public interface RatingService {
    void createRating(Rating rating);
    Rating updateRating(Rating rating);
    void deleteRating(Long id);
    double getAverageRatingByMenuId(Long menuId);
    @Async
    CompletableFuture<Double> getAverageRatingByMenuIdAsync(Long menuId);
}