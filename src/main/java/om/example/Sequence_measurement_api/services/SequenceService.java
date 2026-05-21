package om.example.Sequence_measurement_api.services;

import om.example.Sequence_measurement_api.models.Sequence;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SequenceService {

    private final Map<Long, Sequence> sequenceStore = new ConcurrentHashMap<>();

    public Sequence decode(String input, String sourceIP) {
        Sequence sequence = new Sequence();
        sequence.setInput(input.toLowerCase());
        sequence.setSourceIP(sourceIP);  // store the client IP

        if (!sequence.isValid()) {
            sequence.setValue(List.of("invalid sequence format"));
            sequenceStore.put(sequence.getId(), sequence);
            return sequence;
        }

        String normalized = sequence.getInput();
        List<String> results = new ArrayList<>();
        int i = 0;

        while (i < normalized.length()) {

            int count = 0;
            while (i < normalized.length()) {
                char c = normalized.charAt(i);
                i++;
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
                    char c = normalized.charAt(i);
                    i++;
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

        sequence.setValue(results);
        sequenceStore.put(sequence.getId(), sequence);
        return sequence;
    }

    public Sequence update(long id, String newInput) {
        Sequence sequence = sequenceStore.get(id);
        if (sequence == null) return null;

        sequence.setInput(newInput.toLowerCase());

        if (!sequence.isValid()) {
            sequence.setValue(List.of("invalid sequence format"));
            sequenceStore.put(id, sequence);
            return sequence;
        }

        String normalized = sequence.getInput();
        List<String> results = new ArrayList<>();
        int i = 0;

        while (i < normalized.length()) {

            int count = 0;
            while (i < normalized.length()) {
                char c = normalized.charAt(i);
                i++;
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
                    char c = normalized.charAt(i);
                    i++;
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

        sequence.setValue(results);
        sequenceStore.put(id, sequence);
        return sequence;
    }

    public boolean delete(long id) {
        if (!sequenceStore.containsKey(id)) return false;
        sequenceStore.remove(id);
        return true;
    }

    private int charToValue(char c) {
        if (c == '_') return 0;
        return c - 'a' + 1;
    }
}