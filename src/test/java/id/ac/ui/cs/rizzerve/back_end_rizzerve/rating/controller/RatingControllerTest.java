package id.ac.ui.cs.rizzerve.back_end_rizzerve.rating.controller;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.rating.model.Rating;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.rating.model.Product;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.rating.model.User;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.rating.service.RatingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class RatingControllerTest {

    private MockMvc mockMvc;
    private RatingService ratingService = Mockito.mock(RatingService.class);
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        RatingController ratingController = new RatingController(ratingService);
        mockMvc = MockMvcBuilders.standaloneSetup(ratingController).build();
    }

    @Test
    void testCreateRating_shouldReturnOk() throws Exception {
        Rating rating = new Rating(1L, new User(1L, "TestUser"), new Product(1L, "Burger"), 4);
        mockMvc.perform(post("/api/ratings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(rating)))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAverageRating_shouldReturnDouble() throws Exception {
        Mockito.when(ratingService.getAverageRatingByProductId(1L)).thenReturn(4.0);
        mockMvc.perform(get("/api/ratings/average/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("4.0"));
    }
}
