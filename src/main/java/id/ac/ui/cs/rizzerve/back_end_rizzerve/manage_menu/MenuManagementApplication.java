package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu;

import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu")
public class MenuManagementApplication {
    public static void main(String[] args) {
        SpringApplication.run(MenuManagementApplication.class, args);
    }
}
