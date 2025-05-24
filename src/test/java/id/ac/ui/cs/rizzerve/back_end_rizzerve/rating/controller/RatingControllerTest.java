package id.ac.ui.cs.rizzerve.back_end_rizzerve.rating.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.model.Menu;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.model.User;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.rating.model.Rating;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.rating.service.RatingService;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.rating.controller.RatingRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class RatingControllerTest {

    private MockMvc mockMvc;
    private RatingService ratingService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        ratingService = Mockito.mock(RatingService.class);
        RatingController ratingController = new RatingController(ratingService);
        mockMvc = MockMvcBuilders.standaloneSetup(ratingController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testCreateRating_shouldReturnOk() throws Exception {
        RatingRequest request = new RatingRequest();
        request.setMenuId(1L);
        request.setUserId(2L);
        request.setRatingValue(4);

        Mockito.doNothing().when(ratingService).createRating(eq(1L), eq(2L), eq(4));

        mockMvc.perform(post("/ratings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateRating_shouldReturnUpdatedRating() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("dummyUser");

        Menu menu = new Menu();
        menu.setId(1L);
        menu.setName("Nasi Goreng");
        menu.setDescription("Enak dan pedas");

        RatingRequest request = new RatingRequest();
        request.setId(10L);
        request.setMenuId(1L);
        request.setUserId(2L);
        request.setRatingValue(5);

        Rating updatedRating = new Rating.RatingBuilder().setId(10L).setUser(user).setMenu(menu).setRatingValue(5).build();

        Mockito.when(ratingService.updateRating(any(RatingRequest.class))).thenReturn(updatedRating);

        mockMvc.perform(put("/ratings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(updatedRating)));
    }

    @Test
    void testDeleteRating_shouldReturnOk() throws Exception {
        Long ratingId = 5L;

        Mockito.doNothing().when(ratingService).deleteRating(ratingId);

        mockMvc.perform(delete("/ratings/delete/{menuId}", ratingId))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAverageRating_shouldReturnDoubleAsync() throws Exception {
        Mockito.when(ratingService.getAverageRatingByMenuIdAsync(1L))
                .thenReturn(CompletableFuture.completedFuture(4.5));

        MvcResult mvcResult = mockMvc.perform(get("/ratings/average/1"))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.average").value(4.5));
    }
}