package om.example.Sequence_measurement_api.services;

import om.example.Sequence_measurement_api.models.Sequence;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SequenceService {

    public Sequence decode(String input) {
        Sequence sequence = new Sequence();

        // Normalize to lowercase to handle capital letters
        sequence.setInput(input.toLowerCase());

        // Validate before decoding
        if (!sequence.isValid()) {
            sequence.setValue(List.of("invalid sequence format"));
            return sequence;
        }

        String normalized = sequence.getInput();
        List<String> results = new ArrayList<>();
        int i = 0;

        while (i < normalized.length()) {

            // Step 1: Decode the count
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

            // Step 2: Decode 'count' values and sum them
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
        return sequence;
    }

    private int charToValue(char c) {
        if (c == '_') return 0;
        return c - 'a' + 1;
    }
}