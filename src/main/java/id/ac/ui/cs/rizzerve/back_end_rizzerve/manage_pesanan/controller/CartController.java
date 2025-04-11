// CartController.java
package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.controller;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model.CartItem;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @PostMapping("/{userId}/add/{menuId}")
    public ResponseEntity<Void> addMenuToCart(@PathVariable Long userId, @PathVariable Long menuId) {
        cartService.addMenuToCart(userId, menuId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{userId}/update/{menuId}")
    public ResponseEntity<Void> updateCartItemQuantity(
            @PathVariable Long userId,
            @PathVariable Long menuId,
            @RequestParam int quantityChange) {
        cartService.updateCartItemQuantity(userId, menuId, quantityChange);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}/remove/{menuId}")
    public ResponseEntity<Void> removeMenuFromCart(@PathVariable Long userId, @PathVariable Long menuId) {
        cartService.removeMenuFromCart(userId, menuId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}/clear")
    public ResponseEntity<Void> clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}/items")
    public ResponseEntity<List<CartItem>> getCartItems(@PathVariable Long userId) {
        List<CartItem> items = cartService.getCartItems(userId);
        return ResponseEntity.ok(items);
    }
}