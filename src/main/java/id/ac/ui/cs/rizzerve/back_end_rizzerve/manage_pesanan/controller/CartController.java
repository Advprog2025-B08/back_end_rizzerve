package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.controller;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.command.*;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model.CartItem;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;
    private final CartCommandInvoker commandInvoker;

    @Autowired
    public CartController(CartService cartService, CartCommandInvoker commandInvoker) {
        this.cartService = cartService;
        this.commandInvoker = commandInvoker;
    }

    @PostMapping("/{userId}/items/{menuId}")
    public DeferredResult<ResponseEntity<CartItem>> addItemToCart(@PathVariable Long userId, @PathVariable Long menuId) {
        DeferredResult<ResponseEntity<CartItem>> deferredResult = new DeferredResult<>(30000L);

        deferredResult.onTimeout(() -> {
            deferredResult.setErrorResult(
                    ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT)
                            .body("Request timeout - operation took too long")
            );
        });

        cartService.addItemToCartAsync(userId, menuId)
                .thenAccept(item -> {
                    deferredResult.setResult(new ResponseEntity<>(item, HttpStatus.CREATED));
                })
                .exceptionally(ex -> {
                    deferredResult.setErrorResult(
                            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                    .body("Error adding item to cart: " + ex.getMessage())
                    );
                    return null;
                });

        return deferredResult;
    }

    @PutMapping("/{userId}/items/{menuId}")
    public DeferredResult<ResponseEntity<?>> updateCartItem(
            @PathVariable Long userId,
            @PathVariable Long menuId,
            @RequestParam int quantityChange) {

        DeferredResult<ResponseEntity<?>> deferredResult = new DeferredResult<>(30000L);

        deferredResult.onTimeout(() -> {
            deferredResult.setErrorResult(
                    ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT)
                            .body("Request timeout - operation took too long")
            );
        });

        cartService.updateCartItemQuantityAsync(userId, menuId, quantityChange)
                .thenAccept(item -> {
                    if (item == null) {
                        deferredResult.setResult(new ResponseEntity<>(HttpStatus.NO_CONTENT));
                    } else {
                        deferredResult.setResult(new ResponseEntity<>(item, HttpStatus.OK));
                    }
                })
                .exceptionally(ex -> {
                    deferredResult.setErrorResult(
                            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                    .body("Error updating cart item: " + ex.getMessage())
                    );
                    return null;
                });

        return deferredResult;
    }

    @DeleteMapping("/{userId}/items/{menuId}")
    public DeferredResult<ResponseEntity<Void>> removeItemFromCart(@PathVariable Long userId, @PathVariable Long menuId) {
        DeferredResult<ResponseEntity<Void>> deferredResult = new DeferredResult<>(30000L);

        deferredResult.onTimeout(() -> {
            deferredResult.setErrorResult(
                    ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT)
                            .body("Request timeout - operation took too long")
            );
        });

        cartService.removeItemFromCartAsync(userId, menuId)
                .thenRun(() -> {
                    deferredResult.setResult(new ResponseEntity<>(HttpStatus.NO_CONTENT));
                })
                .exceptionally(ex -> {
                    deferredResult.setErrorResult(
                            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                    .body("Error removing item from cart: " + ex.getMessage())
                    );
                    return null;
                });

        return deferredResult;
    }

    @GetMapping("/{userId}/items")
    public DeferredResult<ResponseEntity<List<CartItem>>> getCartItems(@PathVariable Long userId) {
        DeferredResult<ResponseEntity<List<CartItem>>> deferredResult = new DeferredResult<>(30000L);

        deferredResult.onTimeout(() -> {
            deferredResult.setErrorResult(
                    ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT)
                            .body("Request timeout - operation took too long")
            );
        });

        cartService.getCartItemsAsync(userId)
                .thenAccept(items -> {
                    deferredResult.setResult(new ResponseEntity<>(items, HttpStatus.OK));
                })
                .exceptionally(ex -> {
                    deferredResult.setErrorResult(
                            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                    .body("Error getting cart items: " + ex.getMessage())
                    );
                    return null;
                });

        return deferredResult;
    }

    @DeleteMapping("/{userId}")
    public DeferredResult<ResponseEntity<Void>> clearCart(@PathVariable Long userId) {
        DeferredResult<ResponseEntity<Void>> deferredResult = new DeferredResult<>(30000L);

        deferredResult.onTimeout(() -> {
            deferredResult.setErrorResult(
                    ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT)
                            .body("Request timeout - operation took too long")
            );
        });

        cartService.clearCartAsync(userId)
                .thenRun(() -> {
                    deferredResult.setResult(new ResponseEntity<>(HttpStatus.NO_CONTENT));
                })
                .exceptionally(ex -> {
                    deferredResult.setErrorResult(
                            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                    .body("Error clearing cart: " + ex.getMessage())
                    );
                    return null;
                });

        return deferredResult;
    }
}