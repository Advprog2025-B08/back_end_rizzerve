package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.repository;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCartIdAndMenuId(Long cartId, Long menuId);

    @Query("SELECT ci FROM CartItem ci JOIN FETCH ci.menu WHERE ci.cartId = :cartId AND ci.menuId = :menuId")
    Optional<CartItem> findByCartIdAndMenuIdWithMenu(@Param("cartId") Long cartId, @Param("menuId") Long menuId);

    @Query("SELECT ci FROM CartItem ci JOIN FETCH ci.menu WHERE ci.cart.id = :cartId")
    List<CartItem> findAllByCartIdWithMenu(@Param("cartId") Long cartId);

    List<CartItem> findAllByCartId(Long cartId);

    @Modifying
    @Query("DELETE FROM CartItem ci WHERE ci.menuId = :menuId")
    void deleteByMenuId(@Param("menuId") Long menuId);
}