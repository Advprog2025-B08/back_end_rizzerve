package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_meja.controller;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_meja.model.Meja;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_meja.service.MejaService;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_meja.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/meja")
public class MejaController {

    private final MejaService mejaService;
    private final MejaSSEController sseController;

    @Autowired
    public MejaController(MejaService mejaService, MejaSSEController sseController) {
        this.mejaService = mejaService;
        this.sseController = sseController;
    }

    @PostMapping("/admin/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createMeja(@RequestBody Meja mejaRequest) {
        if (mejaRequest.getNomor() == null) {
            return ResponseEntity.badRequest().body(Map.of("errorCode", 8200, "message", "Param nomor is invalid"));
        }
        Meja meja = mejaService.createMeja(mejaRequest.getNomor());
        if (meja == null){
            return ResponseEntity.badRequest().body(Map.of("errorCode", 8201, "message", "Meja dengan id tersebut sudah ada!"));
        }

        
        sseController.triggerMejaUpdate();

        return ResponseEntity.ok(Map.of("message", "Meja created successfully", "id", meja.getId()));
    }

    @PostMapping("/admin/update/{meja_id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateMeja(@PathVariable("meja_id") int mejaId, @RequestBody Meja mejaRequest) {
        if (mejaService.getMejaByNomor(mejaId) == null) {
            return ResponseEntity.badRequest().body(Map.of("errorCode", 8201, "message", "Request param meja_id is not found"));
        }
        Meja updated = mejaService.updateMeja(mejaId, mejaRequest.getNomor());
        if (updated == null){
            return ResponseEntity.badRequest().body(Map.of("errorCode", 8200, "message", "Param nomor is invalid"));
        }

        
        sseController.triggerMejaUpdate();

        return ResponseEntity.ok(Map.of("message", "Meja updated successfully", "id", updated.getId()));
    }

    @DeleteMapping("/admin/delete/{meja_id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteMeja(@PathVariable("meja_id") int mejaId) {
        if (mejaService.getMejaByNomor(mejaId) == null) {
            return ResponseEntity.badRequest().body(Map.of("errorCode", 8202, "message", "Request param meja_id is not found"));
        }
        boolean success = mejaService.deleteMeja(mejaId);
        if (!success) {
            return ResponseEntity.badRequest().body(Map.of("errorCode", 8200, "message", "Param nomor is invalid"));
        }

        
        sseController.triggerMejaUpdate();

        return ResponseEntity.ok(Map.of("message", "Meja deleted successfully", "id", mejaId));
    }

    @GetMapping("/user/read")
    public ResponseEntity<?> getAllMeja() {
        List<MejaDTO> list = mejaService.getAllMeja();
        if (list.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("errorCode", 8200, "message", "No meja found"));
        }
        return ResponseEntity.ok(Map.of("meja", list));
    }

    @GetMapping("/user/read/{meja_id}")
    public ResponseEntity<?> getMejaByNomor(@PathVariable("meja_id") int nomor) {
        MejaDTO meja = mejaService.getMejaByNomor(nomor);
        if (meja == null) {
            return ResponseEntity.badRequest().body(Map.of("errorCode", 8202, "message", "Request param meja_id is not found"));
        }
        return ResponseEntity.ok(Map.of("meja", meja));
    }

    @PostMapping("/user/set/{meja_id}")
    public ResponseEntity<?> setUserToMeja(@PathVariable("meja_id") String mejaId, @RequestBody UsernameDTO usernameDto) {
        int idVal = Integer.valueOf(mejaId);
        String username = usernameDto.getUsername();
        Meja result = mejaService.setUserToMeja(idVal, username);

        if (result == null) {
            return ResponseEntity.badRequest().body(Map.of("errorCode", 8200, "message", "Failed to set user to meja."));
        }

        
        sseController.triggerMejaUpdate();

        return ResponseEntity.ok(Map.of("message", "User set to meja successfully"));
    }

    @PostMapping("/user/complete_order/{meja_id}")
    public ResponseEntity<?> removeUserFromMeja(@PathVariable("meja_id") String mejaId) {
        int idVal = Integer.valueOf(mejaId);
        boolean result = mejaService.removeUserFromMeja(idVal);

        if (result) {
            
            sseController.triggerMejaUpdate();
            return ResponseEntity.ok(Map.of("message", "Order completed successfully"));
        }

        return ResponseEntity.badRequest().body(Map.of("errorCode", 8200, "message", "Error completing order"));
    }
}