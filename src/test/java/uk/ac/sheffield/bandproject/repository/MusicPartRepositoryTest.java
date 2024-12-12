package uk.ac.sheffield.bandproject.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import uk.ac.sheffield.bandproject.BandProjectApplication;
import uk.ac.sheffield.bandproject.Model.*;
import uk.ac.sheffield.bandproject.Repository.*;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = BandProjectApplication.class)
public class MusicPartRepositoryTest {

    @Autowired
    private MusicSetRepository musicSetRepository;

    @Autowired
    private MusicPartRepository musicPartRepository;

    @Autowired
    private MusicOrderRepository musicOrderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ParentChildRelationshipRepository parentChildRelationshipRepository;

    @Test
    public void shouldSaveAndRetrieveMusicPartById_whenNewMusicPartIsCreated() {
        MusicSet musicSet = new MusicSet(null, "TestMusicSet", "TestComposer", "TestArranger",
                Boolean.TRUE, new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
        musicSetRepository.save(musicSet);

        MusicPart musicPart = new MusicPart(null, "TestPart", musicSet, new HashSet<>());
        musicPartRepository.save(musicPart);

        List<MusicPart> musicParts = musicPartRepository.findByMusicSetId(musicSet.getId());
        assertEquals(1, musicParts.size());
        assertEquals("TestPart", musicParts.get(0).getPartName());
    }

    @Test
    public void shouldReturnAllMusicPartsByOwnerIdAndOrderStatus() {
        User owner = new User(null, "test@test.com", "password", "1234567",
                "TestUser", null, new HashSet<>(), new HashSet<>(), new HashSet<>(),
                new HashSet<>(), new HashSet<>(), new HashSet<>());
        userRepository.save(owner);

        MusicSet musicSet = new MusicSet(null, "TestMusicSet", "TestComposer", "TestArranger",
                Boolean.TRUE, new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
        musicSetRepository.save(musicSet);

        MusicOrder musicOrder = new MusicOrder(null, owner, null, LocalDate.of(2024,1,1),
                "FULFILLED", new HashSet<>());
        musicOrderRepository.save(musicOrder);

        MusicPart musicPart = new MusicPart(null, "TestPart", musicSet, new HashSet<>());
        musicPartRepository.save(musicPart);

        // Add music order to owner to fulfill the many-to-one relationship
        owner.getMusicOrders().add(musicOrder);
        userRepository.save(owner);

        // Add music part to music order to fulfill the many-to-many relationship
        musicOrder.getMusicParts().add(musicPart);
        musicOrderRepository.save(musicOrder);

        // Add music order to music part to fulfill the many-to-many relationship
        musicPart.getMusicOrders().add(musicOrder);
        musicPartRepository.save(musicPart);

        List<MusicPart> musicParts = musicPartRepository.findAllByOwnerIdAndFulfilledOrders(owner.getId());
        assertEquals(1, musicParts.size());
    }

    @Test
    public void shouldReturnAllMusicPartsByChildIdAndOrderStatus() {
        User parent = new User(null, "test@test.com", "password", "1234567",
                "TestParent", null, new HashSet<>(), new HashSet<>(), new HashSet<>(),
                new HashSet<>(), new HashSet<>(), new HashSet<>());
        userRepository.save(parent);

        User child = new User(null, "test@test.com", "password", "1234567",
                "TestChild", null, new HashSet<>(), new HashSet<>(), new HashSet<>(),
                new HashSet<>(), new HashSet<>(), new HashSet<>());
        userRepository.save(child);

        ParentChildRelationship parentChildRelationship = new ParentChildRelationship(null, parent, child);
        parentChildRelationshipRepository.save(parentChildRelationship);

        // Link parent to child
        child.setParentRelationship(parentChildRelationship);
        userRepository.save(child);

        // Link children to parent
        parent.getChildrenRelationships().add(parentChildRelationship);
        userRepository.save(parent);

        MusicSet musicSet = new MusicSet(null, "TestMusicSet", "TestComposer", "TestArranger",
                Boolean.TRUE, new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
        musicSetRepository.save(musicSet);

        MusicOrder musicOrder = new MusicOrder(null, parent, child, LocalDate.of(2024,1,1),
                "FULFILLED", new HashSet<>());
        musicOrderRepository.save(musicOrder);

        MusicPart musicPart = new MusicPart(null, "TestPart", musicSet, new HashSet<>());
        musicPartRepository.save(musicPart);

        // Add music order to child to fulfill the many-to-one relationship
        child.getMusicOrders().add(musicOrder);
        userRepository.save(child);

        // Add music part to music order to fulfill the many-to-many relationship
        musicOrder.getMusicParts().add(musicPart);
        musicOrderRepository.save(musicOrder);

        // Add music order to music part to fulfill the many-to-many relationship
        musicPart.getMusicOrders().add(musicOrder);
        musicPartRepository.save(musicPart);

        List<MusicPart> musicParts = musicPartRepository.findAllByChildIdAndFulfilledOrders(child.getId());
        assertEquals(1, musicParts.size());
    }

    @Test
    public void shouldSaveAndRetrieveSpecificMusicPart() {
        MusicSet musicSet = new MusicSet(null, "TestMusicSet", "TestComposer", "TestArranger",
                Boolean.TRUE, new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
        musicSetRepository.save(musicSet);

        MusicPart musicPart = new MusicPart(null, "TestPart", musicSet, new HashSet<>());
        musicPartRepository.save(musicPart);

        Optional<MusicPart> foundMusicPart = musicPartRepository.findSpecificMusicPart(musicPart.getPartName(),
                musicSet.getTitle(), musicSet.getArranger());
        assertTrue(foundMusicPart.isPresent());
        assertEquals("TestPart", foundMusicPart.get().getPartName());
    }
}