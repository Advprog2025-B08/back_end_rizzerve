package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.exception;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    public ResponseEntity<Map<String, String>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        // Implement this method
    }
    
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // Implement this method
    }
    
    public ResponseEntity<Map<String, String>> handleGeneralExceptions(Exception ex) {
        // Implement this method
    }
}