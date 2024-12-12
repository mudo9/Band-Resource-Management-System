package uk.ac.sheffield.bandproject.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.ac.sheffield.bandproject.Model.MusicSetNote;

public interface MusicSetNoteRepository extends JpaRepository<MusicSetNote, Long> {
}
