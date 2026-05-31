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

    public static SequenceHistory fromSequence(Sequence sequence) {
        SequenceHistory history = new SequenceHistory();
        history.id = sequence.getId();
        history.timestamp = sequence.getTimestamp();
        history.input = sequence.getInput();
        history.sourceIP = sequence.getSourceIP();

        List<Object> values = sequence.getValue();
        if (values == null || values.isEmpty()) {
            history.value = "";
        } else {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < values.size(); i++) {
                if (i > 0) sb.append(",");
                sb.append(values.get(i));
            }
            history.value = sb.toString();
        }
        return history;
    }

    public Sequence toSequence() {
        Sequence sequence = new Sequence();
        sequence.setId(this.id);
        sequence.setTimestamp(this.timestamp);
        sequence.setInput(this.input);
        sequence.setSourceIP(this.sourceIP);

        if (this.value == null || this.value.isEmpty()) {
            sequence.setValue(new ArrayList<>());
        } else {
            String[] parts = this.value.split(",");
            List<Object> parsed = new ArrayList<>();
            for (String part : parts) {
                try {
                    parsed.add(Integer.parseInt(part.trim()));
                } catch (NumberFormatException e) {
                    parsed.add(part.trim());
                }
            }
            sequence.setValue(parsed);
        }
        return sequence;
    }

    public Long getId() { return id; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getValue() { return value; }
    public String getInput() { return input; }
    public String getSourceIP() { return sourceIP; }
}