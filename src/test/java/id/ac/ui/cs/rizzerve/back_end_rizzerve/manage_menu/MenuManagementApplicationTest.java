package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ExtendWith(MockitoExtension.class)
class MenuManagementApplicationTest {

    @Mock
    private ConfigurableApplicationContext context;

    @Test
    void testApplicationAnnotations() {
        assertTrue(MenuManagementApplication.class.isAnnotationPresent(Configuration.class));
        assertTrue(MenuManagementApplication.class.isAnnotationPresent(ComponentScan.class));
        
        ComponentScan componentScan = 
            MenuManagementApplication.class.getAnnotation(ComponentScan.class);
        assertEquals("id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu", componentScan.value()[0]);
    }

    @Test
    void applicationCanBeInstantiated() {
        MenuManagementApplication application = new MenuManagementApplication();
        assertNotNull(application);
    }

    @Test
    void testMainMethodExecutesWithoutException() {
        assertDoesNotThrow(() -> {
            // Test that main method exists and can be called
            String[] args = {};
            try (MockedStatic<SpringApplication> springApp = mockStatic(SpringApplication.class)) {
                springApp.when(() -> SpringApplication.run(MenuManagementApplication.class, args))
                        .thenReturn(context);
                
                MenuManagementApplication.main(args);
                
                springApp.verify(() -> SpringApplication.run(MenuManagementApplication.class, args));
            }
        });
    }

    @Test
    void testMainMethodWithArguments() {
        String[] args = {"--server.port=8081", "--spring.profiles.active=test"};
        
        try (MockedStatic<SpringApplication> springApp = mockStatic(SpringApplication.class)) {
            springApp.when(() -> SpringApplication.run(MenuManagementApplication.class, args))
                    .thenReturn(context);
            
            MenuManagementApplication.main(args);
            
            springApp.verify(() -> SpringApplication.run(MenuManagementApplication.class, args));
        }
    }

    @Test
    void testComponentScanAnnotationValue() {
        ComponentScan componentScan = MenuManagementApplication.class.getAnnotation(ComponentScan.class);
        assertNotNull(componentScan);
        assertEquals(1, componentScan.value().length);
        assertEquals("id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu", componentScan.value()[0]);
    }

    @Test
    void testConfigurationAnnotationPresent() {
        Configuration configuration = MenuManagementApplication.class.getAnnotation(Configuration.class);
        assertNotNull(configuration);
    }

    @Test
    void testClassIsPublic() {
        assertTrue(java.lang.reflect.Modifier.isPublic(MenuManagementApplication.class.getModifiers()));
    }

    @Test
    void testMainMethodIsPublicAndStatic() throws NoSuchMethodException {
        java.lang.reflect.Method mainMethod = MenuManagementApplication.class.getMethod("main", String[].class);
        assertTrue(java.lang.reflect.Modifier.isPublic(mainMethod.getModifiers()));
        assertTrue(java.lang.reflect.Modifier.isStatic(mainMethod.getModifiers()));
    }

    @Test
    void testPackageStructure() {
        String expectedPackage = "id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu";
        assertEquals(expectedPackage, MenuManagementApplication.class.getPackage().getName());
    }
}