package uk.ac.sheffield.bandproject.Service;

import uk.ac.sheffield.bandproject.Model.Band;
import uk.ac.sheffield.bandproject.Model.MusicPart;
import uk.ac.sheffield.bandproject.Model.MusicSet;

import java.util.List;
import java.util.Optional;

public interface MusicSetService {
    List<MusicSet> getAllMusicSets();
    Optional<MusicSet> getMusicSetById(Long id);
    MusicSet saveMusicSet(MusicSet musicSet);
    MusicSet updateMusicSet(Long id, MusicSet updatedMusicSet);
    List<MusicSet> getMusicSetsByBand(String bandName);
    MusicSet addBandToMusicSet(Long MusicSetId, Long BandId);
    void deletePractice(Long musicSetId);
}
