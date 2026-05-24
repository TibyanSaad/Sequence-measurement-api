package om.example.Sequence_measurement_api.services;

import om.example.Sequence_measurement_api.models.Sequence;
import om.example.Sequence_measurement_api.models.SequenceHistory;
import om.example.Sequence_measurement_api.repositories.SequenceRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SequenceService {

    // Hard cap on a single number's z-chain length. 100 consecutive z's would
    // already be 2600+, which is well beyond any plausible legitimate input.
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
            List<String> err = new ArrayList<String>();
            err.add("invalid sequence format");
            sequence.setValue(err);
            return saveAndReturn(sequence);
        }

        sequence.setValue(computeValues(sequence.getInput()));
        return saveAndReturn(sequence);
    }

    public List<Sequence> getAll() {
        List<SequenceHistory> rows = sequenceRepo.findAll();
        List<Sequence> out = new ArrayList<Sequence>();
        for (SequenceHistory row : rows) {
            out.add(row.toSequence());
        }
        return out;
    }

    /**
     * Returns persistence entities directly, with no domain conversion.
     * Useful for raw inspection/debugging. Note: this leaks the JPA mapping
     * shape to whoever calls it — caller beware.
     */
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
            List<String> err = new ArrayList<String>();
            err.add("invalid sequence format");
            sequence.setValue(err);
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

    // Saves the domain Sequence by converting to SequenceHistory, then maps
    // the persisted id back onto the domain object so the caller sees the
    // generated id without needing a second conversion.
    private Sequence saveAndReturn(Sequence sequence) {
        SequenceHistory saved = sequenceRepo.save(SequenceHistory.fromSequence(sequence));
        sequence.setId(saved.getId());
        return sequence;
    }

    /**
     * Decode the input into a list of per-package sums.
     * Any malformed package short-circuits processing: the error message is
     * appended to the result list and decoding stops.
     */
    private List<String> computeValues(String normalized) {
        List<String> results = new ArrayList<String>();
        int i = 0;

        while (i < normalized.length()) {
            // ---- Read the package count ----
            int count = 0;
            int zChain = 0;
            boolean countTerminated = false;

            while (i < normalized.length()) {
                char c = normalized.charAt(i++);
                if (c == 'z') {
                    zChain++;
                    // Edge case: very large z-chain in the count.
                    // Caps runaway / abusive inputs. Same cap applies to value reads below.
                    if (zChain > MAX_Z_CHAIN) {
                        results.add("malformed package: number exceeds maximum allowed size");
                        return results;
                    }
                    count += 26;
                } else {
                    count += charToValue(c);
                    countTerminated = true;
                    break;
                }
            }

            // Edge case: trailing z with no terminator (also covers "input is only z's
            // with no terminator anywhere" — that's just this case when it triggers on
            // the very first package).
            if (!countTerminated) {
                results.add("malformed package: unterminated number");
                return results;
            }

            // Edge case: zero count. An underscore as the count, or any sequence
            // of characters summing to zero (only '_' can do this since 'z' adds 26),
            // produces an empty package whose sum is 0. This also covers the
            // "underscore as count giving zero packages" case — it's the same code path.
            if (count == 0) {
                results.add("0");
                continue;
            }

            // ---- Read 'count' values and sum them ----
            int sum = 0;
            int valuesRead = 0;

            for (int j = 0; j < count; j++) {
                if (i >= normalized.length()) {
                    // Ran out of input mid-package. Handled below after the loop.
                    break;
                }

                int value = 0;
                int valueZChain = 0;
                boolean valueTerminated = false;

                while (i < normalized.length()) {
                    char c = normalized.charAt(i++);
                    if (c == 'z') {
                        valueZChain++;
                        // Edge case: very large z-chain inside a value.
                        if (valueZChain > MAX_Z_CHAIN) {
                            results.add("malformed package: number exceeds maximum allowed size");
                            return results;
                        }
                        value += 26;
                    } else {
                        value += charToValue(c);
                        valueTerminated = true;
                        break;
                    }
                }

                // Edge case: a value's z-chain never terminated (input ended mid-number).
                if (!valueTerminated) {
                    results.add("malformed package: unterminated number");
                    return results;
                }

                sum += value;
                valuesRead++;
            }

            // Edge case: fewer values than count. Covers two of your listed cases:
            //   - "fewer values than count" (general)
            //   - "single character that is only a count with no values" (specific:
            //     count > 0, valuesRead == 0)
            if (valuesRead < count) {
                results.add("malformed package: expected " + count + " values but found " + valuesRead);
                return results;
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