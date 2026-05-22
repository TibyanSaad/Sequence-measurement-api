package om.example.Sequence_measurement_api.services;

import om.example.Sequence_measurement_api.models.Sequence;
import om.example.Sequence_measurement_api.repositories.SequenceRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SequenceService {

    private final SequenceRepo sequenceRepo;

    public SequenceService(SequenceRepo sequenceRepo) {
        this.sequenceRepo = sequenceRepo;
    }

    public Sequence decode(String input, String sourceIP) {
        Sequence sequence = new Sequence();
        sequence.setInput(input.toLowerCase());
        sequence.setSourceIP(sourceIP);

        if (!sequence.isValid()) {
            sequence.setValue(List.of("invalid sequence format"));
            return sequenceRepo.save(sequence);
        }

        sequence.setValue(computeValues(sequence.getInput()));
        return sequenceRepo.save(sequence);
    }

    public List<Sequence> getAll() {
        return sequenceRepo.findAll();
    }

    public Sequence getById(long id) {
        return sequenceRepo.findById(id).orElse(null);
    }

    public Sequence update(long id, String newInput) {
        Sequence sequence = sequenceRepo.findById(id).orElse(null);
        if (sequence == null) return null;

        sequence.setInput(newInput.toLowerCase());

        if (!sequence.isValid()) {
            sequence.setValue(List.of("invalid sequence format"));
            return sequenceRepo.save(sequence);
        }

        sequence.setValue(computeValues(sequence.getInput()));
        return sequenceRepo.save(sequence);
    }

    public boolean delete(long id) {
        if (!sequenceRepo.existsById(id)) return false;
        sequenceRepo.deleteById(id);
        return true;
    }

    private List<String> computeValues(String normalized) {
        List<String> results = new ArrayList<>();
        int i = 0;

        while (i < normalized.length()) {
            int count = 0;
            while (i < normalized.length()) {
                char c = normalized.charAt(i++);
                if (c == 'z') {
                    count += 26;
                } else {
                    count += charToValue(c);
                    break;
                }
            }

            int sum = 0;
            for (int j = 0; j < count && i < normalized.length(); j++) {
                int value = 0;
                while (i < normalized.length()) {
                    char c = normalized.charAt(i++);
                    if (c == 'z') {
                        value += 26;
                    } else {
                        value += charToValue(c);
                        break;
                    }
                }
                sum += value;
            }

            results.add(String.valueOf(sum));
        }

        return results;
    }

    private int charToValue(char c) {
        if (c == '_') return 0;
        return c - 'a' + 1;
    }
}