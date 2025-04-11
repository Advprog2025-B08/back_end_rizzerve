package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class MenuDTO {
    private Long id;
    
    @NotBlank(message = "Menu name is required")
    private String name;
    private String description;
    private String url;
    private String icon;
    private Integer displayOrder;
    private Boolean isActive = true;
}
