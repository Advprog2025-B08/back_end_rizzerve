package id.ac.ui.cs.rizzerve.back_end_rizzerve.rating.controller;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.model.Menu;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.model.User;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.rating.model.Rating;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.rating.service.RatingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.concurrent.CompletableFuture;

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
        User user = new User();
        user.setId(1L);
        user.setUsername("TestUser");
        user.setPassword("pass");
        user.setRole("CUSTOMER");

        Menu menu = new Menu();
        menu.setId(1L);
        menu.setName("Burger");

        Rating rating = Rating.builder()
                .id(1L)
                .user(user)
                .menu(menu)
                .ratingValue(4)
                .build();

        mockMvc.perform(post("/ratings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(rating)))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAverageRating_shouldReturnDoubleAsync() throws Exception {
        Mockito.when(ratingService.getAverageRatingByMenuIdAsync(1L))
                .thenReturn(CompletableFuture.completedFuture(4.0));

        MvcResult mvcResult = mockMvc.perform(get("/ratings/average/1"))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(content().string("4.0"));
    }

}