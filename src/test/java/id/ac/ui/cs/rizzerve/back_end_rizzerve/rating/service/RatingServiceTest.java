package id.ac.ui.cs.rizzerve.back_end_rizzerve.rating.service;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.model.Menu;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.model.User;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.repository.MenuRepository;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.repository.UserRepository;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.rating.controller.RatingRequest;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.rating.model.Rating;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.rating.repository.RatingRepository;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.rating.service.strategy.SimpleAverageStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RatingServiceTest {

    private RatingRepository ratingRepository;
    private MenuRepository menuRepository;
    private UserRepository userRepository;
    private RatingServiceImpl ratingService;

    private User user;
    private Menu menu;

    @BeforeEach
    void setUp() {
        ratingRepository = mock(RatingRepository.class);
        menuRepository = mock(MenuRepository.class);
        userRepository = mock(UserRepository.class);
        ratingService = new RatingServiceImpl(ratingRepository, menuRepository, userRepository);

        // Dummy user and menu
        user = new User();
        user.setId(1L);
        user.setUsername("Daniel");
        user.setPassword("pass");
        user.setRole("CUSTOMER");

        menu = new Menu();
        menu.setId(1L);
        menu.setName("Nasi Goreng");
    }

    @Test
    void testCreateRating() {
        when(menuRepository.findById(1L)).thenReturn(Optional.of(menu));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        ratingService.createRating(1L, 1L, 5);

        ArgumentCaptor<Rating> captor = ArgumentCaptor.forClass(Rating.class);
        verify(ratingRepository).save(captor.capture());

        Rating savedRating = captor.getValue();
        assertEquals(menu, savedRating.getMenu());
        assertEquals(user, savedRating.getUser());
        assertEquals(5, savedRating.getRatingValue());
    }

    @Test
    void testUpdateRating() {
        Rating existingRating = new Rating();
        existingRating.setId(1L);
        existingRating.setRatingValue(3);

        RatingRequest request = new RatingRequest();
        request.setId(1L);
        request.setRatingValue(4);

        when(ratingRepository.findById(1L)).thenReturn(Optional.of(existingRating));
        when(ratingRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Rating updated = ratingService.updateRating(request);

        assertEquals(4, updated.getRatingValue());
    }

    @Test
    void testDeleteRating() {
        Rating rating = new Rating();
        when(ratingRepository.findById(99L)).thenReturn(Optional.of(rating));

        ratingService.deleteRating(99L);

        verify(ratingRepository).deleteById(99L);
    }


    @Test
    void testGetAverageRatingByMenuIdAsync() throws Exception {
        Rating r1 = new Rating();
        r1.setRatingValue(4);
        Rating r2 = new Rating();
        r2.setRatingValue(2);
        Rating r3 = new Rating();
        r3.setRatingValue(5);

        when(ratingRepository.findAllByMenuId(10L)).thenReturn(List.of(r1, r2, r3));

        CompletableFuture<Double> futureAvg = ratingService.getAverageRatingByMenuIdAsync(10L);
        Double avg = futureAvg.get();

        assertEquals(3.666, avg, 0.01);
    }
}