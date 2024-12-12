package uk.ac.sheffield.bandproject.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.ac.sheffield.bandproject.Model.Instrument;

public interface InstrumentRepository extends JpaRepository<Instrument, Long> {
}
