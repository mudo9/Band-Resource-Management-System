package uk.ac.sheffield.bandproject.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uk.ac.sheffield.bandproject.Model.Band;
import uk.ac.sheffield.bandproject.Model.MusicSet;

import java.util.List;

public interface MusicSetRepository  extends JpaRepository<MusicSet, Long> {
    //Retrieves a list of MusicSet entities associated with a specific band by the band's name
    @Query("SELECT m FROM MusicSet m JOIN m.bands b WHERE b.name = :bandName")
    List<MusicSet> findByBandName(@Param("bandName") String bandName);
}
