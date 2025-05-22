package id.ac.ui.cs.rizzerve.back_end_rizzerve.rating.controller;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.rating.model.Rating;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.rating.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/ratings")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    @PostMapping
    public ResponseEntity<Void> createRating(@RequestBody RatingRequest request) {
        ratingService.createRating(request.getMenuId(), request.getUserId(),  request.getRatingValue());
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<Rating> updateRating(@RequestBody RatingRequest request) {
        Rating updated = ratingService.updateRating(request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/delete/{menuId}")
    public ResponseEntity<Void> deleteRating(@PathVariable("menuId") Long id) {
        ratingService.deleteRating(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/average/{menuId}")
    public CompletableFuture<ResponseEntity<Double>> getAverageRating(@PathVariable("menuId") Long menuId) {
        return ratingService.getAverageRatingByMenuIdAsync(menuId)
                .thenApply(ResponseEntity::ok);
    }
}