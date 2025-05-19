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
        user.setRole("USER");

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
    void testUpdateRating_shouldReturnUpdatedRating() throws Exception {
        User user = new User();
        user.setId(2L);
        user.setUsername("UpdateUser");
        user.setPassword("pass");
        user.setRole("USER");

        Menu menu = new Menu();
        menu.setId(2L);
        menu.setName("Pizza");

        Rating ratingToUpdate = Rating.builder()
                .id(10L)
                .user(user)
                .menu(menu)
                .ratingValue(3)
                .build();

        Mockito.when(ratingService.updateRating(Mockito.any(Rating.class))).thenReturn(ratingToUpdate);

        mockMvc.perform(put("/ratings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ratingToUpdate)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(ratingToUpdate)));
    }

    @Test
    void testDeleteRating_shouldReturnOk() throws Exception {
        Long ratingIdToDelete = 5L;

        Mockito.doNothing().when(ratingService).deleteRating(ratingIdToDelete);

        mockMvc.perform(delete("/ratings/delete/{menuId}", ratingIdToDelete))
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