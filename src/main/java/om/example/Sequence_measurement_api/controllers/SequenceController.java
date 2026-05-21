package om.example.Sequence_measurement_api.controllers;

import jakarta.servlet.http.HttpServletRequest;
import om.example.Sequence_measurement_api.models.Sequence;
import om.example.Sequence_measurement_api.services.SequenceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SequenceController {

    private final SequenceService sequenceService;

    public SequenceController(SequenceService sequenceService) {
        this.sequenceService = sequenceService;
    }

    @GetMapping("/convert-measurements")
    public ResponseEntity<List<String>> convertMeasurements(
            @RequestParam String input,
            HttpServletRequest request) {

        String clientIP = getClientIP(request);
        Sequence sequence = sequenceService.decode(input, clientIP);

        if (!sequence.isValid()) {
            return ResponseEntity.badRequest().body(List.of("invalid sequence format"));
        }

        return ResponseEntity.ok(sequence.getValue());
    }

    @PutMapping("/convert-measurements/{id}")
    public ResponseEntity<List<String>> updateMeasurement(
            @PathVariable long id,
            @RequestParam String input) {

        Sequence updated = sequenceService.update(id, input);

        if (updated == null) {
            return ResponseEntity.notFound().build();
        }

        if (!updated.isValid()) {
            return ResponseEntity.badRequest().body(List.of("invalid sequence format"));
        }

        return ResponseEntity.ok(updated.getValue());
    }

    @DeleteMapping("/convert-measurements/{id}")
    public ResponseEntity<String> deleteMeasurement(@PathVariable long id) {
        boolean deleted = sequenceService.delete(id);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok("Sequence " + id + " deleted successfully");
    }

    // Handles proxies and load balancers as well
    private String getClientIP(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isEmpty()) {
            return forwarded.split(",")[0].trim();  // first IP in chain is the real client
        }
        return request.getRemoteAddr();
    }
}