package id.ac.ui.cs.rizzerve.back_end_rizzerve.checkout.controller;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.checkout.dto.CheckoutRequest;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.checkout.model.Checkout;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.checkout.service.CheckoutService;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model.Cart;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.Valid;

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

    // Endpoint untuk membuat checkout
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Checkout createCheckout(@Valid @RequestBody CheckoutRequest checkoutRequest) {
        // Cari cart berdasarkan cartId yang diterima
        Cart cart = cartRepository.findById(checkoutRequest.getCartId())
                .orElseThrow(() -> new RuntimeException("Cart not found")); // Jika cart tidak ditemukan, lempar exception

        // Panggil service untuk membuat checkout
        return checkoutService.createCheckout(cart.getId());
    }
}
