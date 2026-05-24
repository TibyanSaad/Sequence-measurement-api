package om.example.Sequence_measurement_api.models;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * JPA persistence representation of a Sequence. Stores the value list as a
 * comma-separated string in the OUTPUT column. Convert to/from the Sequence
 * domain object via fromSequence() / toSequence().
 */
@Entity
@Table(name = "SEQUENCES")
public class SequenceHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_gen")
    @SequenceGenerator(name = "seq_gen", sequenceName = "SEQUENCE_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "TIMESTAMP")
    private LocalDateTime timestamp;

    @Column(name = "OUTPUT")
    private String value;

    @Column(name = "INPUT")
    private String input;

    @Column(name = "SOURCE_IP")
    private String sourceIP;

    public SequenceHistory() {}

    /**
     * Build a persistence entity from a domain Sequence.
     * The value list is joined with commas; an empty list maps to an empty string.
     */
    public static SequenceHistory fromSequence(Sequence sequence) {
        SequenceHistory history = new SequenceHistory();
        history.id = sequence.getId();
        history.timestamp = sequence.getTimestamp();
        history.input = sequence.getInput();
        history.sourceIP = sequence.getSourceIP();

        List<String> values = sequence.getValue();
        if (values == null || values.isEmpty()) {
            history.value = "";
        } else {
            // Manual join to keep this stream/lambda-free.
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < values.size(); i++) {
                if (i > 0) sb.append(",");
                sb.append(values.get(i));
            }
            history.value = sb.toString();
        }
        return history;
    }

    /**
     * Reconstruct a domain Sequence from this entity.
     * Splits the stored OUTPUT column on commas back into a list.
     */
    public Sequence toSequence() {
        Sequence sequence = new Sequence();
        sequence.setId(this.id);
        sequence.setTimestamp(this.timestamp);
        sequence.setInput(this.input);
        sequence.setSourceIP(this.sourceIP);

        if (this.value == null || this.value.isEmpty()) {
            sequence.setValue(new ArrayList<String>());
        } else {
            // Arrays.asList returns a fixed-size list; wrap in ArrayList so callers can mutate.
            sequence.setValue(new ArrayList<String>(Arrays.asList(this.value.split(","))));
        }
        return sequence;
    }

    public Long getId() { return id; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getValue() { return value; }
    public String getInput() { return input; }
    public String getSourceIP() { return sourceIP; }
}