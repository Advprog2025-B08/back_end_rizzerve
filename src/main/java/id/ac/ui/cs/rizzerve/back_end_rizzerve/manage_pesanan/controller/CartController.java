package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.controller;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.command.*;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model.CartItem;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;
import java.util.concurrent.CompletableFuture;

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
        DeferredResult<ResponseEntity<CartItem>> deferredResult = new DeferredResult<>();

        CompletableFuture.supplyAsync(() -> {
            CartCommand command = new AddToCartCommand(cartService, userId, menuId);
            return (CartItem) commandInvoker.executeCommand(command);
        }).thenApply(item -> {
            return new ResponseEntity<>(item, HttpStatus.CREATED);
        }).whenComplete((result, ex) -> {
            if (ex != null) {
                deferredResult.setErrorResult(ex);
            } else {
                deferredResult.setResult(result);
            }
        });

        return deferredResult;
    }

    @PutMapping("/{userId}/items/{menuId}")
    public DeferredResult<ResponseEntity<?>> updateCartItem(
            @PathVariable Long userId,
            @PathVariable Long menuId,
            @RequestParam int quantityChange) {

        DeferredResult<ResponseEntity<?>> deferredResult = new DeferredResult<>();

        CompletableFuture.supplyAsync(() -> {
            CartCommand command = new UpdateCartCommand(cartService, userId, menuId, quantityChange);
            return (CartItem) commandInvoker.executeCommand(command);
        }).thenApply(item -> {
            if (item == null) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(item, HttpStatus.OK);
        }).whenComplete((result, ex) -> {
            if (ex != null) {
                deferredResult.setErrorResult(ex);
            } else {
                deferredResult.setResult(result);
            }
        });

        return deferredResult;
    }

    @DeleteMapping("/{userId}/items/{menuId}")
    public DeferredResult<ResponseEntity<Void>> removeItemFromCart(@PathVariable Long userId, @PathVariable Long menuId) {
        DeferredResult<ResponseEntity<Void>> deferredResult = new DeferredResult<>();

        CompletableFuture.runAsync(() -> {
            CartCommand command = new RemoveFromCartCommand(cartService, userId, menuId);
            commandInvoker.executeCommand(command);
        }).thenRun(() -> {
            deferredResult.setResult(new ResponseEntity<>(HttpStatus.NO_CONTENT));
        }).exceptionally(ex -> {
            deferredResult.setErrorResult(ex);
            return null;
        });

        return deferredResult;
    }

    @GetMapping("/{userId}/items")
    public DeferredResult<ResponseEntity<List<CartItem>>> getCartItems(@PathVariable Long userId) {
        DeferredResult<ResponseEntity<List<CartItem>>> deferredResult = new DeferredResult<>();

        CompletableFuture.supplyAsync(() -> {
            CartCommand command = new GetCartItemsCommand(cartService, userId);
            return (List<CartItem>) commandInvoker.executeCommand(command);
        }).thenApply(items -> {
            return new ResponseEntity<>(items, HttpStatus.OK);
        }).whenComplete((result, ex) -> {
            if (ex != null) {
                deferredResult.setErrorResult(ex);
            } else {
                deferredResult.setResult(result);
            }
        });

        return deferredResult;
    }

    @DeleteMapping("/{userId}")
    public DeferredResult<ResponseEntity<Void>> clearCart(@PathVariable Long userId) {
        DeferredResult<ResponseEntity<Void>> deferredResult = new DeferredResult<>();

        CompletableFuture.runAsync(() -> {
            CartCommand command = new ClearCartCommand(cartService, userId);
            commandInvoker.executeCommand(command);
        }).thenRun(() -> {
            deferredResult.setResult(new ResponseEntity<>(HttpStatus.NO_CONTENT));
        }).exceptionally(ex -> {
            deferredResult.setErrorResult(ex);
            return null;
        });

        return deferredResult;
    }
}