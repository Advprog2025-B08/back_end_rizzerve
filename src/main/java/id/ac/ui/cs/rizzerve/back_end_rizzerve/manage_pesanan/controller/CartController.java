package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.controller;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.command.*;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model.CartItem;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<CartItem> addItemToCart(@PathVariable Long userId, @PathVariable Long menuId) {
        CartCommand command = new AddToCartCommand(cartService, userId, menuId);
        CartItem item = (CartItem) commandInvoker.executeCommand(command);
        return new ResponseEntity<>(item, HttpStatus.CREATED);
    }

    @PutMapping("/{userId}/items/{menuId}")
    public ResponseEntity<?> updateCartItem(
            @PathVariable Long userId,
            @PathVariable Long menuId,
            @RequestParam int quantityChange) {

        CartCommand command = new UpdateCartCommand(cartService, userId, menuId, quantityChange);
        CartItem item = (CartItem) commandInvoker.executeCommand(command);

        if (item == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(item, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}/items/{menuId}")
    public ResponseEntity<Void> removeItemFromCart(@PathVariable Long userId, @PathVariable Long menuId) {
        CartCommand command = new RemoveFromCartCommand(cartService, userId, menuId);
        commandInvoker.executeCommand(command);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{userId}/items")
    public ResponseEntity<List<CartItem>> getCartItems(@PathVariable Long userId) {
        CartCommand command = new GetCartItemsCommand(cartService, userId);
        List<CartItem> items = (List<CartItem>) commandInvoker.executeCommand(command);
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> clearCart(@PathVariable Long userId) {
        CartCommand command = new ClearCartCommand(cartService, userId);
        commandInvoker.executeCommand(command);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}