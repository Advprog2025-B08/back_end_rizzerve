package id.ac.ui.cs.rizzerve.back_end_rizzerve.checkout.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CheckoutResponse {
    private Long id;
    private Long cartId;
    private Long userId;
    private Integer totalPrice;
    private Boolean isSubmitted;
    private String createdAt; // Bisa diformat sebagai String ISO Date
}