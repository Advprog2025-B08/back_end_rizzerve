package id.ac.ui.cs.rizzerve.back_end_rizzerve.checkout.dto;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model.Cart;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CheckoutRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testCartIdCannotBeNull() {
        CheckoutRequest request = new CheckoutRequest();

        Set<ConstraintViolation<CheckoutRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size());
        assertEquals("Cart ID cannot be null", violations.iterator().next().getMessage());
    }

    @Test
    void testCartIdValid() {
        CheckoutRequest request = new CheckoutRequest();
        request.setCartId(1L);


        Set<ConstraintViolation<CheckoutRequest>> violations = validator.validate(request);


        assertEquals(0, violations.size(), "No validation errors should be present");
    }

    @Test
    void testSetCart() {
        Cart dummyCart = new Cart();
        dummyCart.setId(5L);
        CheckoutRequest request = new CheckoutRequest();


        request.setCart(dummyCart);
        assertEquals(5L, request.getCartId());
    }

}
