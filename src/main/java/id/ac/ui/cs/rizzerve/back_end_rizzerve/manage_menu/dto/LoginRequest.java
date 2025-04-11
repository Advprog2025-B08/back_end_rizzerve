package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class LoginRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
