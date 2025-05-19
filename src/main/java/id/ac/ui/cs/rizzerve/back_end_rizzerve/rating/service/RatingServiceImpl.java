package id.ac.ui.cs.rizzerve.back_end_rizzerve.rating.service;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.rating.model.Rating;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.rating.repository.RatingRepository;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.rating.service.strategy.AverageRatingStrategy;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.rating.service.strategy.SimpleAverageStrategy;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.rating.service.helper.RatingCalculatorHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class RatingServiceImpl implements RatingService {

    private RatingRepository ratingRepository;
    private AverageRatingStrategy averageRatingStrategy;
    private RatingCalculatorHelper helper = RatingCalculatorHelper.getInstance();

    public RatingServiceImpl(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
        this.averageRatingStrategy = new SimpleAverageStrategy();
    }

    @Override
    public void createRating(Rating rating) {
        ratingRepository.save(rating);
    }

    @Override
    public Rating updateRating(Rating rating) {
        ratingRepository.save(rating);
        return rating;
    }

    @Override
    public void deleteRating(Long id) {
        ratingRepository.deleteById(id);
    }

    @Override
    public double getAverageRatingByMenuId(Long menuId) {
        List<Rating> ratings = ratingRepository.findAllByMenuId(menuId);
        List<Integer> values = ratings.stream()
                .map(Rating::getRatingValue)
                .collect(Collectors.toList());
        return averageRatingStrategy.calculateAverage(values);
    }

    @Async
    @Override
    public CompletableFuture<Double> getAverageRatingByMenuIdAsync(Long menuId) {
        List<Rating> ratings = ratingRepository.findAllByMenuId(menuId);
        List<Integer> values = ratings.stream()
                .map(Rating::getRatingValue)
                .collect(Collectors.toList());
        double avg = averageRatingStrategy.calculateAverage(values);
        return CompletableFuture.completedFuture(avg);
    }

    public void setAverageRatingStrategy(AverageRatingStrategy strategy) {
        this.averageRatingStrategy = strategy;
    }
}