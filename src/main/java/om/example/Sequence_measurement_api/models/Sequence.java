package om.example.Sequence_measurement_api.models;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

public class Sequence {
    private static final AtomicLong counter = new AtomicLong();
    private final long id = counter.incrementAndGet();
    LocalDateTime timestamp = LocalDateTime.now();
    List<String> value = new CopyOnWriteArrayList<>();
    private String input;
    private String sourceIpAddress;

    public long getId() {
        return id;
    }

    public List<String> getValue() {
        return value;
    }

    public void setValue(List<String> value) {
        this.value = value;
    }

    public boolean isValid(){
        return true;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }
}
