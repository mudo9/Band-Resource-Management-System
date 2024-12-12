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
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = BandProjectApplication.class)
public class MusicOrderRepositoryTest {

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
    public void shouldReturnAllMusicOrdersByOwnerId() {
        User owner = new User(null, "test@test.com", "password", "1234567",
                "TestUser", null, new HashSet<>(), new HashSet<>(), new HashSet<>(),
                new HashSet<>(), new HashSet<>(), new HashSet<>());
        userRepository.save(owner);

        MusicSet musicSet = new MusicSet(null, "TestMusicSet", "TestComposer", "TestArranger",
                Boolean.TRUE, new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
        musicSetRepository.save(musicSet);

        MusicOrder musicOrder = new MusicOrder(null, owner, null, LocalDate.of(2024,1,1),
                "TEST STATUS", new HashSet<>());
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

        List<MusicOrder> musicOrders = musicOrderRepository.findByOwnerId(owner.getId());
        assertEquals(1, musicOrders.size());
    }

    @Test
    public void shouldReturnAllMusicOrdersByStatusAndOwnerId() {
        User owner = new User(null, "test@test.com", "password", "1234567",
                "TestUser", null, new HashSet<>(), new HashSet<>(), new HashSet<>(),
                new HashSet<>(), new HashSet<>(), new HashSet<>());
        userRepository.save(owner);

        MusicSet musicSet = new MusicSet(null, "TestMusicSet", "TestComposer", "TestArranger",
                Boolean.TRUE, new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
        musicSetRepository.save(musicSet);

        MusicOrder musicOrder = new MusicOrder(null, owner, null, LocalDate.of(2024,1,1),
                "TEST STATUS", new HashSet<>());
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

        List<MusicOrder> musicOrders = musicOrderRepository.findByStatusAndOwnerId(musicOrder.getStatus(), owner.getId());
        assertEquals(1, musicOrders.size());
    }

    @Test
    public void shouldReturnAllMusicOrdersByStatusAndChildId() {
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
                "TEST STATUS", new HashSet<>());
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

        List<MusicOrder> musicOrders = musicOrderRepository.findByStatusAndChildId(musicOrder.getStatus(), child.getId());
        assertEquals(1, musicOrders.size());
    }

    @Test
    public void shouldReturnAllMusicOrdersByStatus() {
        User owner = new User(null, "test@test.com", "password", "1234567",
                "TestUser", null, new HashSet<>(), new HashSet<>(), new HashSet<>(),
                new HashSet<>(), new HashSet<>(), new HashSet<>());
        userRepository.save(owner);

        MusicSet musicSet = new MusicSet(null, "TestMusicSet", "TestComposer", "TestArranger",
                Boolean.TRUE, new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
        musicSetRepository.save(musicSet);

        MusicOrder musicOrder = new MusicOrder(null, owner, null, LocalDate.of(2024,1,1),
                "TEST STATUS", new HashSet<>());
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

        List<MusicOrder> musicOrders = musicOrderRepository.findByStatus(musicOrder.getStatus());
        assertEquals(1, musicOrders.size());
    }
}
