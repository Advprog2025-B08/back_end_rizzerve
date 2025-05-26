package id.ac.ui.cs.rizzerve.back_end_rizzerve.rating.repository;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.rating.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

    @Query("SELECT r FROM Rating r WHERE r.menu.id = :menuId")
    List<Rating> findAllByMenuId(@Param("menuId") Long menuId);

    @Query("SELECT r FROM Rating r WHERE r.user.id = :userId AND r.menu.id = :menuId")
    Optional<Rating> findByUserIdAndMenuId(@Param("userId") Long userId, @Param("menuId") Long menuId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Rating r WHERE r.menu.id = :menuId")
    void deleteByMenuId(@Param("menuId") Long menuId);
}