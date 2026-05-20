package om.example.Sequence_measurement_api.services;

import om.example.Sequence_measurement_api.models.Sequence;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SequenceService {

    public Sequence decode(String input) {
        Sequence sequence = new Sequence();
        sequence.setInput(input);

        List<String> results = new ArrayList<>();
        int i = 0;

        while (i < input.length()) {

            // Step 1: Decode the count
            int count = 0;
            while (i < input.length()) {
                char c = input.charAt(i);
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
            for (int j = 0; j < count && i < input.length(); j++) {
                int value = 0;
                while (i < input.length()) {
                    char c = input.charAt(i);
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

    // Maps character to its value
    // '_' = 0, 'a' = 1, 'b' = 2, ..., 'y' = 25
    private int charToValue(char c) {
        if (c == '_') return 0;
        return c - 'a' + 1;
    }
}
