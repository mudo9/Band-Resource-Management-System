package uk.ac.sheffield.bandproject.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import uk.ac.sheffield.bandproject.BandProjectApplication;
import uk.ac.sheffield.bandproject.Model.ParentChildRelationship;
import uk.ac.sheffield.bandproject.Model.User;
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
public class ParentChildRelationshipRepositoryTest {

    @Autowired
    private ParentChildRelationshipRepository parentChildRelationshipRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void shouldReturnAllParentChildrenRelationshipByParent() {
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

        List<ParentChildRelationship> parentChildRelationships = parentChildRelationshipRepository.findByParent(parent);
        assertEquals(1, parentChildRelationships.size());
    }

    @Test
    public void shouldReturnAllParentChildrenRelationshipByChild() {
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

        // Link parent to children
        child.setParentRelationship(parentChildRelationship);
        userRepository.save(child);

        // Link children to parent
        parent.getChildrenRelationships().add(parentChildRelationship);
        userRepository.save(parent);

        Optional<ParentChildRelationship> foundParentChildRelationship = parentChildRelationshipRepository.findByChild(child);
        assertTrue(foundParentChildRelationship.isPresent());
        assertEquals("TestParent", foundParentChildRelationship.get().getParent().getFullName());
    }
}
