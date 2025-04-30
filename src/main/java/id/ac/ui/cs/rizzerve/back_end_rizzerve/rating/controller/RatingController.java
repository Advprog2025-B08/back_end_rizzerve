package id.ac.ui.cs.rizzerve.back_end_rizzerve.rating.controller;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.rating.model.Rating;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.rating.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ratings")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    @PostMapping
    public ResponseEntity<Void> createRating(@RequestBody Rating rating) {
        ratingService.createRating(rating);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<Rating> updateRating(@RequestBody Rating rating) {
        Rating updated = ratingService.updateRating(rating);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRating(@PathVariable Long id) {
        ratingService.deleteRating(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/average/{productId}")
    public Double getAverageRating(@PathVariable("productId") Long productId) {
        return ratingService.getAverageRatingByProductId(productId);
    }
}
