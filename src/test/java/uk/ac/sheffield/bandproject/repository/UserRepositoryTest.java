package uk.ac.sheffield.bandproject.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import uk.ac.sheffield.bandproject.BandProjectApplication;
import uk.ac.sheffield.bandproject.Model.Band;
import uk.ac.sheffield.bandproject.Model.ParentChildRelationship;
import uk.ac.sheffield.bandproject.Model.Role;
import uk.ac.sheffield.bandproject.Model.User;
import uk.ac.sheffield.bandproject.Repository.BandRepository;
import uk.ac.sheffield.bandproject.Repository.ParentChildRelationshipRepository;
import uk.ac.sheffield.bandproject.Repository.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = BandProjectApplication.class)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ParentChildRelationshipRepository parentChildRelationshipRepository;

    @Autowired
    private BandRepository bandRepository;

    @Test
    public void shouldReturnUserByEmailAndNoParent() {
        User user = new User(null, "test@test.com", "password", "1234567",
                "TestUser", null, new HashSet<>(), new HashSet<>(), new HashSet<>(),
                new HashSet<>(), new HashSet<>(), new HashSet<>());
        userRepository.save(user);

        Optional<User> foundUser = userRepository.findByEmailAndNoParent("test@test.com");
        assertTrue(foundUser.isPresent());
        assertEquals("1234567", foundUser.get().getEffectivePhoneNumber());
    }

    @Test
    public void shouldReturnUserByParentId() {
        User parent = new User(null, "test@test.com", "password", "1234567",
                "TestParent", null, new HashSet<>(), new HashSet<>(), new HashSet<>(),
                new HashSet<>(), new HashSet<>(), new HashSet<>());
        userRepository.save(parent);

        User child = new User(null, "test@test.com", "password", "1234567",
                "TestChild", null, new HashSet<>(), new HashSet<>(), new HashSet<>(),
                new HashSet<>(), new HashSet<>(), new HashSet<>());
        userRepository.save(child);

        // Link parent to child
        ParentChildRelationship parentChildRelationship = new ParentChildRelationship(null, parent, child);
        parentChildRelationshipRepository.save(parentChildRelationship);

        child.setParentRelationship(parentChildRelationship);
        userRepository.save(child);

        // Link children to parent
        parent.getChildrenRelationships().add(parentChildRelationship);
        userRepository.save(parent);

        List<User> children = userRepository.findChildrenByParentId(parent.getId());
        assertEquals(1, children.size());
        assertEquals("TestChild", children.get(0).getFullName());
    }

    @Test
    public void shouldReturnUserByFullName() {
        User user = new User(null, "test@test.com", "password", "1234567",
                "TestUser", null, new HashSet<>(), new HashSet<>(), new HashSet<>(),
                new HashSet<>(), new HashSet<>(), new HashSet<>());
        userRepository.save(user);

        Optional<User> foundUser = userRepository.findByFullName("TestUser");
        assertTrue(foundUser.isPresent());
        assertEquals("1234567", foundUser.get().getEffectivePhoneNumber());
    }

    @Test
    public void shouldReturnUsersByBandName() {
        Band band = new Band(null, "TestBand", new HashSet<>(), new HashSet<>(), new HashSet<>());
        bandRepository.save(band);

        User user = new User(null, "test@test.com", "password", "1234567",
                "TestUser", null, new HashSet<>(), new HashSet<>(), new HashSet<>(),
                new HashSet<>(), new HashSet<>(), new HashSet<>());
        user.getBands().add(band);
        userRepository.save(user);

        band.getUsers().add(user);
        bandRepository.save(band);

        List<User> bandMembers = userRepository.findByBandName(band.getName());
        assertEquals(1, bandMembers.size());
        assertEquals("TestUser", bandMembers.get(0).getFullName());
    }

    @Test
    public void shouldReturnBandsByUserId() {
        Band band = new Band(null, "TestBand", new HashSet<>(), new HashSet<>(), new HashSet<>());
        bandRepository.save(band);

        User user = new User(null, "test@test.com", "password", "1234567", "TestUser",
                null, new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>(),
                new HashSet<>(), new HashSet<>());
        user.getBands().add(band);
        userRepository.save(user);

        band.getUsers().add(user);
        bandRepository.save(band);

        List<Band> bands = userRepository.findBandsByUserId(user.getId());
        assertEquals(1, bands.size());
        assertEquals("TestBand", bands.get(0).getName());
    }
}
