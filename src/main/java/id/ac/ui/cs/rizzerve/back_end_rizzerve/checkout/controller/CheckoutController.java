package id.ac.ui.cs.rizzerve.back_end_rizzerve.checkout.controller;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.checkout.dto.CheckoutRequest;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.checkout.dto.CheckoutResponse;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.checkout.model.Checkout;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.checkout.service.CheckoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.Valid;

import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/api/checkouts")
@Validated
public class CheckoutController {

    private final CheckoutService checkoutService;

    @Autowired
    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @GetMapping("/submitted")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllSubmittedCheckouts() {
        return ResponseEntity.ok(checkoutService.getSubmittedCheckouts());
    }

    @GetMapping
    public ResponseEntity<?> getCheckoutsByUserId(@RequestParam Long userId) {
        try {
            Checkout checkout = checkoutService.findCheckoutsByUserId(userId);

            CheckoutResponse responseDTO = CheckoutResponse.builder()
                    .id(checkout.getId())
                    .build();

            return ResponseEntity.ok(responseDTO);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/{checkoutId}")
    public ResponseEntity<?> getCheckoutDetails(@PathVariable Long checkoutId) {
        Optional<Checkout> optionalCheckout = checkoutService.findById(checkoutId);
        if (optionalCheckout.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Checkout not found");
        }
        Checkout checkout = optionalCheckout.get();

        CheckoutResponse responseDTO = CheckoutResponse.builder()
                .id(checkout.getId())
                .cartId(checkout.getCart() != null ? checkout.getCart().getId() : null)
                .userId(checkout.getUser() != null ? checkout.getUser().getId() : null)
                .totalPrice(checkout.getTotalPrice())
                .isSubmitted(checkout.getIsSubmitted())
                .createdAt(checkout.getCreatedAt() != null ? checkout.getCreatedAt().toString() : null)
                .build();

        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createCheckout(@Valid @RequestBody CheckoutRequest checkoutRequest) {
        try {
            Checkout checkout = checkoutService.createCheckout(checkoutRequest.getCartId());
            CheckoutResponse response = CheckoutResponse.builder()
                    .id(checkout.getId())
                    .build();
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PatchMapping("/{cartId}/items/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> updateCartItemQuantity(
            @PathVariable Long cartId,
            @PathVariable Long itemId,
            @RequestParam int deltaQuantity
    ) {
        try {
            checkoutService.updateCartItemQuantity(cartId, itemId, deltaQuantity);
            return ResponseEntity.ok("Cart item quantity updated");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PatchMapping("/{checkoutId}/submit")
    public ResponseEntity<?> submitCheckout(@PathVariable Long checkoutId) {
        try {
            Checkout submitted = checkoutService.submitCheckout(checkoutId);
            CheckoutResponse responseDTO = CheckoutResponse.builder()
                    .id(submitted.getId())
                    .isSubmitted(submitted.getIsSubmitted())
                    .build();
            return ResponseEntity.ok(responseDTO);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{checkoutId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> cancelCheckout(@PathVariable Long checkoutId) {
        try {
            checkoutService.deleteCheckout(checkoutId);
            return ResponseEntity.ok("Checkout canceled");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{checkoutId}/processed")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteProcessedCheckout(@PathVariable Long checkoutId) {
        try {
            checkoutService.deleteCheckoutAfterProcessing(checkoutId);
            return ResponseEntity.ok("Checkout has been successfully processed and removed from the system.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
