package id.ac.ui.cs.rizzerve.back_end_rizzerve.repository;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.model.Rating;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RatingRepository {

    private Map<Long, Rating> storage = new HashMap<>();

    public void save(Rating rating) {
        storage.put(rating.getId(), rating);
    }

    public Optional<Rating> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public void deleteById(Long id) {
        storage.remove(id);
    }
}
