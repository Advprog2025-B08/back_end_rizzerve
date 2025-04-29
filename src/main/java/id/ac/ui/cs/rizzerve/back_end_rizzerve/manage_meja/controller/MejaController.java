package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_meja.controller;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_meja.model.Meja;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_meja.service.MejaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/meja")
public class MejaController {

    private final MejaService mejaService;

    public MejaController(MejaService mejaService) {
        this.mejaService = mejaService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createMeja(@RequestBody Meja mejaRequest) {
        if (mejaRequest.getNomor() == null) {
            return ResponseEntity.badRequest().body(Map.of("errorCode", 8200, "message", "Param nomor is invalid"));
        }
        Meja meja = mejaService.createMeja(mejaRequest.getNomor());
        return ResponseEntity.ok(Map.of("message", "Meja created successfully", "id", meja.getId()));
    }

    @PostMapping("/update/{meja_id}")
    public ResponseEntity<?> updateMeja(@PathVariable("meja_id") int mejaId, @RequestBody Meja mejaRequest) {
        if (mejaService.getMejaByNomor(mejaId) == null) {
            return ResponseEntity.badRequest().body(Map.of("errorCode", 8201, "message", "Request param meja_id is not found"));
        }
        Meja updated = mejaService.updateMeja(mejaId, mejaRequest.getNomor());
        if (updated == null){
            return ResponseEntity.badRequest().body(Map.of("errorCode", 8200, "message", "Param nomor is invalid"));
        }
        return ResponseEntity.ok(Map.of("message", "Meja updated successfully", "id", updated.getId()));
    }

    @DeleteMapping("/delete/{meja_id}")
    public ResponseEntity<?> deleteMeja(@PathVariable("meja_id") int mejaId) {
        if (mejaService.getMejaByNomor(mejaId) == null) {
            return ResponseEntity.badRequest().body(Map.of("errorCode", 8202, "message", "Request param meja_id is not found"));
        }
        boolean success = mejaService.deleteMeja(mejaId);
        if (!success) {
            return ResponseEntity.badRequest().body(Map.of("errorCode", 8200, "message", "Param nomor is invalid"));
        }
        return ResponseEntity.ok(Map.of("message", "Meja deleted successfully", "id", mejaId));
    }

    @GetMapping("/read")
    public ResponseEntity<?> getAllMeja() {
        List<Meja> list = mejaService.getAllMeja();
        if (list.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("errorCode", 8200, "message", "No meja found"));
        }
        return ResponseEntity.ok(Map.of("meja", list));
    }

    @GetMapping("/read/{meja_id}")
    public ResponseEntity<?> getMejaByNomor(@PathVariable("meja_id") int nomor) {
        Meja meja = mejaService.getMejaByNomor(nomor);
        if (meja == null) {
            ResponseEntity.badRequest().body(Map.of("errorCode", 8202, "message", "Request param meja_id is not found"));
        }
        return ResponseEntity.ok(Map.of("meja", meja));
    }

    @PostMapping("/set/{meja_id}")
    public ResponseEntity<?> setUserToMeja(@PathVariable("meja_id") String mejaId, @RequestBody String userId) {
        int id_val = Integer.valueOf(mejaId);
        int usr_id = Integer.valueOf(userId);
        Meja result = mejaService.setUserToMeja(id_val, usr_id);

        if (result == null) {
            return ResponseEntity.badRequest().body(Map.of("errorCode", 8200, "message", "Failed to set user to meja."));
        }

        return ResponseEntity.ok(Map.of("message", result));
    }

    @PostMapping("/complete_order/{meja_id}")
    public ResponseEntity<?> removeUserFromMeja(@PathVariable("meja_id") String mejaId) {
        int id_val = Integer.valueOf(mejaId);
        boolean result = mejaService.removeUserFromMeja(id_val);
        if (result){
            return ResponseEntity.ok(Map.of("message", result));
        }
        return ResponseEntity.badRequest().body(Map.of("errorCode", result ? 8204 : 8200, "message", "Error"));
    }
}
