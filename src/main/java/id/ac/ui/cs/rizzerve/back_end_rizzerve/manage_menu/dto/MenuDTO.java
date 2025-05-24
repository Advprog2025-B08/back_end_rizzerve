package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.dto;

import jakarta.validation.constraints.NotBlank;

public class MenuDTO {
    private Long id;
    
    @NotBlank(message = "Menu name is required")
    private String name;
    private String description;
    private String url;
    private String icon;
    private Integer displayOrder;
    private Boolean isActive = true;

    public MenuDTO(Long id, String name, String description, String url, String icon, Integer displayOrder, Boolean isActive) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.url = url;
        this.icon = icon;
        this.displayOrder = displayOrder;
        this.isActive = isActive;
    }
    public MenuDTO() {
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getIcon() {
        return icon;
    }
    public void setIcon(String icon) {
        this.icon = icon;
    }
    public Integer getDisplayOrder() {
        return displayOrder;
    }
    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }
    public Boolean getIsActive() {
        return isActive;
    }
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}
