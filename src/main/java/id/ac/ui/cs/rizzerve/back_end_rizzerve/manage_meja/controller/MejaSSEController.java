package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_meja.controller;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_meja.dto.MejaDTO;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_meja.service.MejaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/meja/admin")
public class MejaSSEController {

    private final MejaService mejaService;
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Autowired
    public MejaSSEController(MejaService mejaService) {
        this.mejaService = mejaService;

        scheduler.scheduleAtFixedRate(this::sendMejaUpdates, 0, 2, TimeUnit.SECONDS);
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> streamMejaUpdates(
            @RequestParam(required = false)
            HttpServletRequest request) {

        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);

        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitter.onError((ex) -> emitters.remove(emitter));

        emitters.add(emitter);

        try {
            List<MejaDTO> allMeja = mejaService.getAllMeja();
            emitter.send(SseEmitter.event()
                    .name("meja-update")
                    .data(allMeja));
        } catch (IOException e) {
            emitter.completeWithError(e);
        }

        return ResponseEntity.ok()
                .body(emitter);
    }

    private void sendMejaUpdates() {
        if (emitters.isEmpty()) {
            return;
        }

        List<MejaDTO> allMeja = mejaService.getAllMeja();
        List<SseEmitter> deadEmitters = new CopyOnWriteArrayList<>();

        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event()
                        .name("meja-update")
                        .data(allMeja));
            } catch (IOException e) {
                deadEmitters.add(emitter);
            }
        }

        emitters.removeAll(deadEmitters);
    }

    public void triggerMejaUpdate() {
        sendMejaUpdates();
    }
}