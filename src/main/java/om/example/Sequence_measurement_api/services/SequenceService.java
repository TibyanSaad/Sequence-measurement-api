package om.example.Sequence_measurement_api.services;

import om.example.Sequence_measurement_api.models.Sequence;
import om.example.Sequence_measurement_api.models.SequenceHistory;
import om.example.Sequence_measurement_api.repositories.SequenceRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SequenceService {

    private static final int MAX_Z_CHAIN = 100;

    private final SequenceRepo sequenceRepo;

    public SequenceService(SequenceRepo sequenceRepo) {
        this.sequenceRepo = sequenceRepo;
    }

    public Sequence decode(String input, String sourceIP) {
        Sequence sequence = new Sequence();
        sequence.setInput(input == null ? null : input.toLowerCase());
        sequence.setSourceIP(sourceIP);

        if (!sequence.isValid()) {
            sequence.setValue(new ArrayList<>());
            return saveAndReturn(sequence);
        }

        sequence.setValue(computeValues(sequence.getInput()));
        return saveAndReturn(sequence);
    }

    public List<Sequence> getAll() {
        List<SequenceHistory> rows = sequenceRepo.findAll();
        List<Sequence> out = new ArrayList<>();
        for (SequenceHistory row : rows) {
            out.add(row.toSequence());
        }
        return out;
    }

    public List<SequenceHistory> getAllHistory() {
        return sequenceRepo.findAll();
    }

    public Sequence getById(long id) {
        SequenceHistory row = sequenceRepo.findById(id).orElse(null);
        if (row == null) return null;
        return row.toSequence();
    }

    public Sequence update(long id, String newInput) {
        SequenceHistory existing = sequenceRepo.findById(id).orElse(null);
        if (existing == null) return null;

        Sequence sequence = existing.toSequence();
        sequence.setInput(newInput == null ? null : newInput.toLowerCase());

        if (!sequence.isValid()) {
            sequence.setValue(new ArrayList<>());
            return saveAndReturn(sequence);
        }

        sequence.setValue(computeValues(sequence.getInput()));
        return saveAndReturn(sequence);
    }

    public boolean delete(long id) {
        if (!sequenceRepo.existsById(id)) return false;
        sequenceRepo.deleteById(id);
        return true;
    }

    private Sequence saveAndReturn(Sequence sequence) {
        SequenceHistory saved = sequenceRepo.save(SequenceHistory.fromSequence(sequence));
        sequence.setId(saved.getId());
        return sequence;
    }

    private List<Object> computeValues(String normalized) {
        List<Object> results = new ArrayList<>();
        int i = 0;

        while (i < normalized.length()) {
            int count = 0;
            int zChain = 0;
            boolean countTerminated = false;

            while (i < normalized.length()) {
                char c = normalized.charAt(i++);
                if (c == 'z') {
                    zChain++;
                    if (zChain > MAX_Z_CHAIN) {
                        return results;
                    }
                    count += 26;
                } else {
                    count += charToValue(c);
                    countTerminated = true;
                    break;
                }
            }

            if (!countTerminated) {
                return results;
            }

            if (count == 0) {
                results.add(0);
                continue;
            }

            int sum = 0;
            int valuesRead = 0;

            for (int j = 0; j < count; j++) {
                if (i >= normalized.length()) {
                    break;
                }

                int value = 0;
                int valueZChain = 0;
                boolean valueTerminated = false;

                while (i < normalized.length()) {
                    char c = normalized.charAt(i++);
                    if (c == 'z') {
                        valueZChain++;
                        if (valueZChain > MAX_Z_CHAIN) {
                            return results;
                        }
                        value += 26;
                    } else {
                        value += charToValue(c);
                        valueTerminated = true;
                        break;
                    }
                }

                if (!valueTerminated) {
                    return results;
                }

                sum += value;
                valuesRead++;
            }

            if (valuesRead < count) {
                return results;
            }

            results.add(sum);
        }

        return results;
    }

    private int charToValue(char c) {
        if (c == '_') return 0;
        return c - 'a' + 1;
    }
}