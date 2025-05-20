package id.ac.ui.cs.rizzerve.back_end_rizzerve.rating.service;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.model.Menu;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.model.User;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.repository.MenuRepository;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.repository.UserRepository;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.rating.controller.RatingRequest;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.rating.model.Rating;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.rating.repository.RatingRepository;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.rating.service.strategy.AverageRatingStrategy;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.rating.service.strategy.SimpleAverageStrategy;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.rating.service.helper.RatingCalculatorHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class RatingServiceImpl implements RatingService {

    private RatingRepository ratingRepository;
    private UserRepository userRepository;
    private MenuRepository menuRepository;
    private AverageRatingStrategy averageRatingStrategy;
    private RatingCalculatorHelper helper = RatingCalculatorHelper.getInstance();

    public RatingServiceImpl(RatingRepository ratingRepository, MenuRepository menuRepository, UserRepository userRepository) {
        this.ratingRepository = ratingRepository;
        this.menuRepository = menuRepository;
        this.userRepository = userRepository;
        this.averageRatingStrategy = new SimpleAverageStrategy();
    }

    @Override
    @Transactional
    public void createRating(Long menuId, Long userId, int ratingValue) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new RuntimeException("Menu not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Rating rating = new Rating();
        rating.setMenu(menu);
        rating.setUser(user);
        rating.setRatingValue(ratingValue);

        ratingRepository.save(rating);
    }

    @Override
    public Rating updateRating(RatingRequest request) {
        Rating existing = ratingRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("Rating not found"));

        existing.setRatingValue(request.getRatingValue());

        return ratingRepository.save(existing);
    }


    @Override
    public void deleteRating(Long id) {
        ratingRepository.deleteById(id);
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