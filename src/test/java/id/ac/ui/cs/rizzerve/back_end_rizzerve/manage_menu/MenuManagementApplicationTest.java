package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ConfigurableApplicationContext;

@ExtendWith(MockitoExtension.class)
class MenuManagementApplicationTest {

    @Mock
    private ConfigurableApplicationContext context;

    @Test
    void testApplicationAnnotations() {
        assertTrue(MenuManagementApplication.class.isAnnotationPresent(org.springframework.context.annotation.Configuration.class));
        assertTrue(MenuManagementApplication.class.isAnnotationPresent(org.springframework.context.annotation.ComponentScan.class));
        
        org.springframework.context.annotation.ComponentScan componentScan = 
            MenuManagementApplication.class.getAnnotation(org.springframework.context.annotation.ComponentScan.class);
        assertEquals("id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu", componentScan.value()[0]);
    }

    @Test
    void applicationCanBeInstantiated() {
        MenuManagementApplication application = new MenuManagementApplication();
        assertNotNull(application);
    }
}