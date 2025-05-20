package id.ac.ui.cs.rizzerve.back_end_rizzerve.rating.service;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.rating.controller.RatingRequest;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.rating.model.Rating;
import org.springframework.scheduling.annotation.Async;

import java.util.concurrent.CompletableFuture;

public interface RatingService {
    void createRating(Long menuId, Long userId, int ratingValue);
    Rating updateRating(RatingRequest request);
    void deleteRating(Long id);
    @Async
    CompletableFuture<Double> getAverageRatingByMenuIdAsync(Long menuId);
}