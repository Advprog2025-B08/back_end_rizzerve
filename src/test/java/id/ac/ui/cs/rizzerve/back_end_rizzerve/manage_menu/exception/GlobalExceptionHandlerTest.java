package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.exception;

import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

@ExtendWith(MockitoExtension.class)
public class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler exceptionHandler;
    
    @Mock
    private MethodArgumentNotValidException methodArgumentNotValidException;
    
    @Mock
    private BindingResult bindingResult;
    
    @Mock
    private FieldError fieldError1;
    
    @Mock
    private FieldError fieldError2;

    @Test
    void handleResourceNotFoundException_ShouldReturnNotFoundStatus() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Resource not found");
        
        ResponseEntity<Map<String, String>> response = exceptionHandler.handleResourceNotFoundException(ex);
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Resource not found", response.getBody().get("error"));
    }
    
    @Test
    void handleValidationExceptions_ShouldReturnBadRequestStatus() {
        when(methodArgumentNotValidException.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(Arrays.asList(fieldError1, fieldError2));
        when(fieldError1.getField()).thenReturn("name");
        when(fieldError1.getDefaultMessage()).thenReturn("Name is required");
        when(fieldError2.getField()).thenReturn("email");
        when(fieldError2.getDefaultMessage()).thenReturn("Email is invalid");
        
        ResponseEntity<Map<String, String>> response = exceptionHandler.handleValidationExceptions(methodArgumentNotValidException);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Name is required", response.getBody().get("name"));
        assertEquals("Email is invalid", response.getBody().get("email"));
    }
    
    @Test
    void handleGeneralExceptions_ShouldReturnInternalServerErrorStatus() {
        Exception ex = new Exception("Unexpected error");
        
        ResponseEntity<Map<String, String>> response = exceptionHandler.handleGeneralExceptions(ex);
        
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An unexpected error occurred: Unexpected error", response.getBody().get("error"));
    }
}