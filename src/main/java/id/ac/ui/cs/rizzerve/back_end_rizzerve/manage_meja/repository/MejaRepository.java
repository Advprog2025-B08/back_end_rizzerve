package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_meja.repository;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_meja.model.Meja;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MejaRepository extends JpaRepository<Meja, String> {

    @Query("SELECT m FROM Meja m")
    List<Meja> getAllMejas();

    @Query("SELECT m FROM Meja m WHERE m.nomor = :nomor")
    Meja getMejaByNomor(@Param("nomor") int nomor);

    @Query("SELECT COUNT(m) = 0 FROM Meja m WHERE m.nomor = :nomor")
    boolean checkUnique(@Param("nomor") int nomor);

    @Query("SELECT m FROM Meja m WHERE m.id = :mejaId")
    Optional<Meja> findById(@Param("mejaId") Long mejaId);

    @Query("SELECT m FROM Meja m WHERE m.nomor = :mejaNum")
    Optional<Meja> findByNomor(@Param("mejaNum") int mejaNum);

    List<Meja> findByUserIsNull();
    List<Meja> findByUserIsNotNull();
    Optional<Meja> findByUser(User user);
}