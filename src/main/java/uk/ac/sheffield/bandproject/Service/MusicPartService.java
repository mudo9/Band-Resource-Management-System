package uk.ac.sheffield.bandproject.Service;

import uk.ac.sheffield.bandproject.Model.MusicPart;

import java.util.List;
import java.util.Optional;

public interface MusicPartService {
    List<MusicPart> getAllMusicParts();
    Optional<MusicPart> getMusicPartById(Long id);
    MusicPart saveMusicPart(MusicPart musicSet);
    MusicPart updateMusicPart(Long id, MusicPart updatedMusicSet);
    List<MusicPart> getPartsByMusicSetId(Long musicSetId);

    MusicPart createMusicPart(Long musicSetId, MusicPart newMusicSet);
    List<MusicPart> getUserMusicPartNeeded(Long userId);
    Optional<MusicPart> getMusicPartForOrder(String musicPartName, String musicSetTitle, String musicSetArranger);
    List<MusicPart> getUserMusicPart(Long ownerId);
    List<MusicPart> getChildMusicPart(Long childId);
}
