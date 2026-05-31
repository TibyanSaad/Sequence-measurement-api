package om.example.Sequence_measurement_api.controllers;

import jakarta.servlet.http.HttpServletRequest;
import om.example.Sequence_measurement_api.models.SequenceHistory;
import om.example.Sequence_measurement_api.services.SequenceService;
import om.example.Sequence_measurement_api.models.Sequence;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class SequenceController {

    private final SequenceService sequenceService;

    public SequenceController(SequenceService sequenceService) {
        this.sequenceService = sequenceService;
    }

    @GetMapping("/convert-measurements")
    public ResponseEntity<List<Object>> convertMeasurements(@RequestParam String input, HttpServletRequest request) {
        String clientIP = getClientIP(request);
        Sequence sequence = sequenceService.decode(input, clientIP);
        if (!sequence.isValid()) {
            return ResponseEntity.badRequest().body(List.of("invalid sequence format"));
        }
        return ResponseEntity.ok(sequence.getValue());
    }

    @GetMapping("/convert-measurements/{id}")
    public ResponseEntity<Sequence> getMeasurementById(@PathVariable long id) {
        Sequence sequence = sequenceService.getById(id);
        if (sequence == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(sequence);
    }

    @GetMapping("/convert-measurements/all")
    public ResponseEntity<List<Sequence>> getAllMeasurements() {
        List<Sequence> all = sequenceService.getAll();
        if (all.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(all);
    }

    @GetMapping("/convert-measurements/history")
    public ResponseEntity<List<SequenceHistory>> getHistory() {
        List<SequenceHistory> history = sequenceService.getAllHistory();
        if (history.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(history);
    }

    @PutMapping("/convert-measurements/{id}")
    public ResponseEntity<List<Object>> updateMeasurement(@PathVariable long id, @RequestBody Map<String, String> body) {
        String input = body.get("input");
        if (input == null || input.isEmpty()) return ResponseEntity.badRequest().body(List.of("input field is required"));
        Sequence updated = sequenceService.update(id, input);
        if (updated == null) return ResponseEntity.notFound().build();
        if (!updated.isValid()) return ResponseEntity.badRequest().body(List.of("invalid sequence format"));
        return ResponseEntity.ok(updated.getValue());
    }

    @DeleteMapping("/convert-measurements/{id}")
    public ResponseEntity<String> deleteMeasurement(@PathVariable long id) {
        boolean deleted = sequenceService.delete(id);
        if (!deleted) return ResponseEntity.notFound().build();
        return ResponseEntity.ok("Sequence " + id + " deleted successfully");
    }

    private String getClientIP(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isEmpty()) return forwarded.split(",")[0].trim();
        return request.getRemoteAddr();
    }
}