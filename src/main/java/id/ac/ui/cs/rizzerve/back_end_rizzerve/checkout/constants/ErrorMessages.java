package id.ac.ui.cs.rizzerve.back_end_rizzerve.checkout.constants;

public final class ErrorMessages {

    private ErrorMessages() {
        // Private constructor to prevent instantiation
    }

    public static final String CART_NOT_FOUND = "Cart not found";
    public static final String CHECKOUT_ALREADY_SUBMITTED =
            "Checkout from this cart has already been submitted. Wait for ADMIN to process your order";
    public static final String CHECKOUT_HAS_NOT_BEEN_SUBMITTED =
            "Checkout has not been submitted yet";
    public static final String CHECKOUT_ALREADY_CREATED =
            "Checkout from this cart has already been created";
    public static final String CHECKOUT_NOT_FOUND = "Checkout not found";
    public static final String CHECKOUT_NOT_SUBMITTED = "Checkout not submitted yet";
    public static final String CHECKOUT_NOT_CREATED = "Checkout not created yet";
    public static final String ITEM_NOT_FOUND_IN_CART = "Item not found in cart";
    public static final String RESULTING_QUANTITY_CAN_NOT_BE_NEGATIVE =
            "Resulting quantity can not be negative";
}

