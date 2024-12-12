package uk.ac.sheffield.bandproject.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.ac.sheffield.bandproject.Model.Band;

import java.util.Optional;

public interface BandRepository extends JpaRepository<Band, Long> {
    Optional<Band> findByName(String bandName);
}
