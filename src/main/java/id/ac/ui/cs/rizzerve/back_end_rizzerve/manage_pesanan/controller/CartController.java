package id.ac.ui.cs.rizzerve.back_end_rizzerve.controller;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.model.Cart;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.model.Product;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping("/{userId}")
    public ResponseEntity<Cart> getCart(@PathVariable Long userId) {
        return cartService.getCartByUserId(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{userId}/items")
    public ResponseEntity<Void> addProductToCart(@PathVariable Long userId, @RequestBody Product product) {
        cartService.addProductToCart(userId, product);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping("/{userId}/items/{productId}")
    public ResponseEntity<Void> updateCartItemQuantity(
            @PathVariable Long userId,
            @PathVariable Long productId,
            @RequestParam int quantityChange) {
        cartService.updateCartItemQuantity(userId, productId, quantityChange);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}/items/{productId}")
    public ResponseEntity<Void> removeProductFromCart(
            @PathVariable Long userId,
            @PathVariable Long productId) {
        cartService.removeProductFromCart(userId, productId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
        return ResponseEntity.ok().build();
    }
}