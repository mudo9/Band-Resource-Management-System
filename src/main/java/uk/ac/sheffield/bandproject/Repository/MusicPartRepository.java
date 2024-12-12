package uk.ac.sheffield.bandproject.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uk.ac.sheffield.bandproject.Model.MusicPart;
import uk.ac.sheffield.bandproject.Model.User;

import java.util.List;
import java.util.Optional;

public interface MusicPartRepository  extends JpaRepository<MusicPart, Long> {
    List<MusicPart> findByMusicSetId(Long musicSetId);

    //Retrieves a list of MusicPart entities that are part of the orders associated with a specific owner
    @Query("SELECT mp FROM MusicPart mp " +
            "JOIN mp.musicOrders mo " +
            "WHERE mo.owner.id = :ownerId AND mo.status = 'FULFILLED'")
    List<MusicPart> findAllByOwnerIdAndFulfilledOrders(@Param("ownerId") Long ownerId);

    //Retrieves a list of MusicPart entities that are part of the orders associated with a specific child
    @Query("SELECT mp FROM MusicPart mp " +
            "JOIN mp.musicOrders mo " +
            "WHERE mo.child.id = :childId AND mo.status = 'FULFILLED'")
    List<MusicPart> findAllByChildIdAndFulfilledOrders(@Param("childId") Long childId);

    //Retrieves a specific MusicPart based on its name, the title of the music set it belongs to, and the arranger
    @Query("SELECT mp FROM MusicPart mp " +
            "WHERE mp.partName = :partName " +
            "AND mp.musicSet.title = :setName " +
            "AND (:arranger IS NULL OR mp.musicSet.arranger = :arranger)")
    Optional<MusicPart> findSpecificMusicPart(@Param("partName") String partName,
                                              @Param("setName") String setName,
                                              @Param("arranger") String arranger);
}

