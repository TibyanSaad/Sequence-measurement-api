package om.example.Sequence_measurement_api.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "SEQUENCES")
public class Sequence {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_gen")
    @SequenceGenerator(name = "seq_gen", sequenceName = "SEQUENCE_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "TIMESTAMP")
    private LocalDateTime timestamp = LocalDateTime.now();

    @Column(name = "OUTPUT")
    private String value;

    @Column(name = "INPUT")
    private String input;

    @Column(name = "SOURCE_IP")
    private String sourceIP;

    public Long getId() { return id; }

    public List<String> getValue() {
        if (value == null || value.isEmpty()) return List.of();
        return Arrays.asList(value.split(","));
    }

    public void setValue(List<String> values) {
        this.value = String.join(",", values);
    }

    public LocalDateTime getTimestamp() { return timestamp; }

    public String getInput() { return input; }
    public void setInput(String input) { this.input = input; }

    public String getSourceIP() { return sourceIP; }
    public void setSourceIP(String sourceIP) { this.sourceIP = sourceIP; }

    public boolean isValid() {
        if (input == null || input.isEmpty()) return false;
        return input.matches("[a-z_]+");
    }
}