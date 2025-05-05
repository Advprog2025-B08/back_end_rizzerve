package id.ac.ui.cs.rizzerve.back_end_rizzerve.rating.repository;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.model.Menu;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.model.User;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.rating.model.Rating;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
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

    public List<Rating> findAllByMenuId(Long menuId) {
        return storage.values().stream()
                .filter(rating -> rating.getMenu().getId().equals(menuId))
                .collect(Collectors.toList());
    }
}