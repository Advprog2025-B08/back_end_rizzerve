package id.ac.ui.cs.rizzerve.back_end_rizzerve.rating.repository;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.rating.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

    @Query("SELECT r FROM Rating r WHERE r.menu.id = :menuId")
    List<Rating> findAllByMenuId(@Param("menuId") Long menuId);
}