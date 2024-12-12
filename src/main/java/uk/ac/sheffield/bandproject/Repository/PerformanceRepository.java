package uk.ac.sheffield.bandproject.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uk.ac.sheffield.bandproject.Model.Band;
import uk.ac.sheffield.bandproject.Model.Performance;

import java.util.List;

public interface PerformanceRepository extends JpaRepository<Performance, Long> {
    //Retrieves a list of distinct Performance entities associated with a band
    @Query("SELECT DISTINCT p FROM Performance p JOIN p.bands b WHERE b.id = :bandId")
    List<Performance> findByBand(@Param("bandId") Long bandId);

}
