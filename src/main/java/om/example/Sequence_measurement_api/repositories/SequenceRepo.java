package om.example.Sequence_measurement_api.repositories;

import om.example.Sequence_measurement_api.models.SequenceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SequenceRepo extends JpaRepository<SequenceHistory, Long> {
}