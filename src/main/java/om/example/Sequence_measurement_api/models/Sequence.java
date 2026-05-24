package om.example.Sequence_measurement_api.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Pure domain object. No persistence concerns — all JPA mapping lives in SequenceHistory.
 * The value field is a plain List<String>; serialization to/from the comma-separated
 * OUTPUT column is the entity's job, not this class's.
 */
public class Sequence {

    private Long id;
    private LocalDateTime timestamp = LocalDateTime.now();
    private String input;
    private String sourceIP;
    private List<String> value = new ArrayList<>();

    public Sequence() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public String getInput() { return input; }
    public void setInput(String input) { this.input = input; }

    public String getSourceIP() { return sourceIP; }
    public void setSourceIP(String sourceIP) { this.sourceIP = sourceIP; }

    public List<String> getValue() {
        return value == null ? new ArrayList<>() : value;
    }

    public void setValue(List<String> value) {
        this.value = value;
    }

    public boolean isValid() {
        if (input == null || input.isEmpty()) return false;
        return input.matches("[a-z_]+");
    }
}
