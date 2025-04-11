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
        if (mejaService.getMejaById(mejaId) == null) {
            return ResponseEntity.badRequest().body(Map.of("errorCode", 8201, "message", "Request param meja_id is not found"));
        }
        Meja updated = mejaService.updateMeja(mejaId, mejaRequest.getNomor());
        if (updated == null){
            return ResponseEntity.badRequest().body(Map.of("errorCode", 8200, "message", "Param nomor is invalid"));
        }
        return ResponseEntity.ok(Map.of("message", "Meja updated successfully", "id", updated.getId()));
    }

    @DeleteMapping("/delete/{meja_id}")
    public ResponseEntity<?> deleteMeja(@PathVariable("meja_id") String mejaId) {
        if (mejaId == null || mejaId.isEmpty()) {
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
    public ResponseEntity<?> getMejaById(@PathVariable("meja_id") String mejaId) {
        int id_val = Integer.valueOf(mejaId);
        Meja meja = mejaService.getMejaById(id_val);
        if (meja == null) {
            ResponseEntity.badRequest().body(Map.of("errorCode", 8202, "message", "Request param meja_id is not found"));
        }
        return ResponseEntity.ok(Map.of("meja", meja));
    }

    @PostMapping("/set/{meja_id}")
    public ResponseEntity<?> setUserToMeja(@PathVariable("meja_id") String mejaId) {
        String result = mejaService.setUserToMeja(mejaId);
        if (result.contains("invalid"))
            return ResponseEntity.badRequest().body(Map.of("errorCode", 8200, "message", result));
        return ResponseEntity.ok(Map.of("message", result));
    }

    @PostMapping("/complete_order/{meja_id}")
    public ResponseEntity<?> removeUserFromMeja(@PathVariable("meja_id") String mejaId) {
        String result = mejaService.removeUserFromMeja(mejaId);
        if (result.contains("invalid") || result.contains("empty"))
            return ResponseEntity.badRequest().body(Map.of("errorCode", result.contains("empty") ? 8204 : 8200, "message", result));
        return ResponseEntity.ok(Map.of("message", result));
    }
}
