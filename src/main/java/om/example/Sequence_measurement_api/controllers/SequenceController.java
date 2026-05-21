package om.example.Sequence_measurement_api.controllers;

import om.example.Sequence_measurement_api.models.Sequence;
import om.example.Sequence_measurement_api.services.SequenceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SequenceController {

    private final SequenceService sequenceService;

    public SequenceController(SequenceService sequenceService) {
        this.sequenceService = sequenceService;
    }

    @GetMapping("/convert-measurements")
    public ResponseEntity<List<String>> convertMeasurements(@RequestParam String input) {
        Sequence sequence = sequenceService.decode(input);

        if (!sequence.isValid()) {
            return ResponseEntity.badRequest().body(List.of("invalid sequence format"));
        }

        return ResponseEntity.ok(sequence.getValue());
    }
}