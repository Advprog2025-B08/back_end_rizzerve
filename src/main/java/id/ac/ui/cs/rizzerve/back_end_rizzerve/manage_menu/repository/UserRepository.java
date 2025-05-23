package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}