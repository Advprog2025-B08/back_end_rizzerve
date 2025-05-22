package id.ac.ui.cs.rizzerve.back_end_rizzerve.checkout.controller;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.checkout.dto.CheckoutRequest;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.checkout.model.Checkout;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.checkout.service.CheckoutService;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model.Cart;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.Valid;

import java.util.Optional;

@RestController
@RequestMapping("/api/checkouts")
@Validated
public class CheckoutController {

    private final CheckoutService checkoutService;
    private final CartRepository cartRepository;

    @Autowired
    public CheckoutController(CheckoutService checkoutService, CartRepository cartRepository) {
        this.checkoutService = checkoutService;
        this.cartRepository = cartRepository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createCheckout(@Valid @RequestBody CheckoutRequest checkoutRequest) {
        Optional<Cart> optionalCart = cartRepository.findById(checkoutRequest.getCartId());
        if (optionalCart.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cart not found");
        }
        Checkout checkout = checkoutService.createCheckout(optionalCart.get().getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(checkout);
    }
}
