package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class ResourceNotFoundExceptionTest {

    @Test
    void testExceptionMessage() {
        String errorMessage = "Resource not found";
        
        ResourceNotFoundException exception = new ResourceNotFoundException(errorMessage);
        
        assertEquals(errorMessage, exception.getMessage());
    }
    
    @Test
    void testExceptionAnnotation() {
        Class<ResourceNotFoundException> clazz = ResourceNotFoundException.class;
        ResponseStatus annotation = clazz.getAnnotation(ResponseStatus.class);
        
        assertNotNull(annotation);
        assertEquals(HttpStatus.NOT_FOUND, annotation.value());
    }
}