package id.ac.ui.cs.rizzerve.back_end_rizzerve.rating.service;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.rating.model.Rating;

public interface RatingService {
    void createRating(Rating rating);
    Rating updateRating(Rating rating);
    void deleteRating(Long id);
    double getAverageRatingByProductId(Long productId);
}
