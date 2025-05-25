package id.ac.ui.cs.rizzerve.back_end_rizzerve.rating.controller;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.rating.model.Rating;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.rating.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/ratings")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> createRating(@RequestBody RatingRequest request) {
        ratingService.createRating(request.getMenuId(), request.getUserId(),  request.getRatingValue());
        return ResponseEntity.ok().build();
    }

    @PutMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Rating> updateRating(@RequestBody RatingRequest request) {
        Rating updated = ratingService.updateRating(request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/delete/{menuId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> deleteRating(@PathVariable("menuId") Long id) {
        ratingService.deleteRating(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/average/{menuId}")
    public CompletableFuture<ResponseEntity<Map<String, Double>>> getAverageRating(@PathVariable("menuId") Long menuId) {
        return ratingService.getAverageRatingByMenuIdAsync(menuId)
                .thenApply(avg -> {
                    Map<String, Double> response = new HashMap<>();
                    response.put("average", avg);
                    return ResponseEntity.ok(response);
                });
    }

    @GetMapping("/user-rating")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, Object>> getUserRating(
            @RequestParam("userId") Long userId, 
            @RequestParam("menuId") Long menuId) {
        Optional<Rating> rating = ratingService.getRatingByUserAndMenu(userId, menuId);
        if (rating.isPresent()) {
            Map<String, Object> response = new HashMap<>();
            response.put("id", rating.get().getId());
            response.put("ratingValue", rating.get().getRatingValue());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}