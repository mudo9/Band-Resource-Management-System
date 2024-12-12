package uk.ac.sheffield.bandproject.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.ac.sheffield.bandproject.Model.Band;
import uk.ac.sheffield.bandproject.Model.ParentChildRelationship;
import uk.ac.sheffield.bandproject.Model.Role;
import uk.ac.sheffield.bandproject.Model.User;
import uk.ac.sheffield.bandproject.Repository.UserRepository;
import uk.ac.sheffield.bandproject.Service.UserServiceImpl;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldReturnUser_whenValidIdIsGiven() {
        User user = new User(1L, "test@test.com", "password", "1234567",
                "TestUser", null, new HashSet<>(), new HashSet<>(), new HashSet<>(),
                new HashSet<>(), new HashSet<>(), new HashSet<>());
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.getUserById(1L);
        assertTrue(foundUser.isPresent());
        assertEquals("test@test.com", foundUser.get().getEmail());
    }

    @Test
    public void shouldReturnAllUsers_whenGetAllUsersIsCalled() {
        User user1 = new User(1L, "test@test.com", "password", "1234567",
                "TestUser1", null, new HashSet<>(), new HashSet<>(), new HashSet<>(),
                new HashSet<>(), new HashSet<>(), new HashSet<>());
        User user2 = new User(2L, "test2@test.com", "password", "9876543",
                "TestUser2", null, new HashSet<>(), new HashSet<>(), new HashSet<>(),
                new HashSet<>(), new HashSet<>(), new HashSet<>());
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<User> users = userService.getAllUsers();
        assertEquals(2, users.size());
        assertEquals("test@test.com", users.get(0).getEmail());
        assertEquals("test2@test.com", users.get(1).getEmail());
    }

    @Test
    public void shouldReturnAllUsers_whenGetUsersByBandIsCalled() {
        Band band = new Band(1L, "TestBand", new HashSet<>(), new HashSet<>(), new HashSet<>());
        Set<Band> bands = new HashSet<>();
        bands.add(band);

        User user1 = new User(1L, "test@test.com", "password", "1234567",
                "TestUser1", null, new HashSet<>(), bands, new HashSet<>(),
                new HashSet<>(), new HashSet<>(), new HashSet<>());
        User user2 = new User(2L, "test2@test.com", "password", "9876543",
                "TestUser2", null, new HashSet<>(), bands, new HashSet<>(),
                new HashSet<>(), new HashSet<>(), new HashSet<>());
        when(userRepository.findByBandName("TestBand")).thenReturn(Arrays.asList(user1, user2));

        List<User> users = userService.getUsersByBand("TestBand");
        assertEquals(2, users.size());
        assertEquals("test@test.com", users.get(0).getEmail());
        assertEquals("test2@test.com", users.get(1).getEmail());
    }

    @Test
    public void shouldReturnAllCommitteeMembers_whenGetCommitteeMembersIsCalled() {
        Set<Role> roles = new HashSet<>();
        roles.add(Role.COMMITTEE_MEMBER);

        User user1 = new User(1L, "test@test.com", "password", "1234567",
                "TestUser1", null, roles, new HashSet<>(), new HashSet<>(),
                new HashSet<>(), new HashSet<>(), new HashSet<>());
        User user2 = new User(2L, "test2@test.com", "password", "9876543",
                "TestUser2", null, roles, new HashSet<>(), new HashSet<>(),
                new HashSet<>(), new HashSet<>(), new HashSet<>());
        when(userRepository.findByRole(Role.COMMITTEE_MEMBER)).thenReturn(Arrays.asList(user1, user2));

        List<User> committeeMembers = userService.getCommitteeMembers();
        assertEquals(2, committeeMembers.size());
        assertEquals("test@test.com", committeeMembers.get(0).getEmail());
        assertEquals("test2@test.com", committeeMembers.get(1).getEmail());
    }

    @Test
    public void shouldUpdateUser_whenPromoteUserToCommitteeMemberIsCalled() {
        Set<Role> roles = new HashSet<>();
        roles.add(Role.COMMITTEE_MEMBER);

        User existingUser = new User(1L, "test@test.com", "password", "1234567",
                "TestUser", null, new HashSet<>(), new HashSet<>(), new HashSet<>(),
                new HashSet<>(), new HashSet<>(), new HashSet<>());
        User promotedUser = new User(1L, "test@test.com", "password", "1234567",
                "TestUser", null, roles, new HashSet<>(), new HashSet<>(),
                new HashSet<>(), new HashSet<>(), new HashSet<>());
        when(userRepository.findByEmailAndNoParent("test@test.com")).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(promotedUser);

        User result = userService.promoteUserToCommitteeMember("test@test.com");
        assertNotNull(result);
        assertTrue(result.getRoles().contains(Role.COMMITTEE_MEMBER));
    }

    @Test
    public void shouldUpdateUser_whenDemoteUserFromCommitteeMemberIsCalled() {
        Set<Role> roles = new HashSet<>();
        roles.add(Role.COMMITTEE_MEMBER);

        User existingUser = new User(1L, "test@test.com", "password", "1234567",
                "TestUser", null, roles, new HashSet<>(), new HashSet<>(),
                new HashSet<>(), new HashSet<>(), new HashSet<>());
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));

        userService.demoteUserFromCommitteeMember(1L);
        verify(userRepository, times(1)).save(existingUser);
        assertFalse(existingUser.getRoles().contains(Role.COMMITTEE_MEMBER));
    }

    @Test
    public void shouldReturnUser_whenValidEmailIsGiven() {
        User user = new User(1L, "test@test.com", "password", "1234567",
                "TestUser", null, new HashSet<>(), new HashSet<>(), new HashSet<>(),
                new HashSet<>(), new HashSet<>(), new HashSet<>());
        when(userRepository.findByEmailAndNoParent("test@test.com")).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.getUserByEmail("test@test.com");
        assertTrue(foundUser.isPresent());
        assertEquals("TestUser", foundUser.get().getFullName());
    }

    @Test
    public void shouldReturnAllChildren_whenGetParentChildrenIsCalled() {
        User parent = new User(1L, "test@test.com", "password", "1234567",
                "TestParent", null, new HashSet<>(), new HashSet<>(), new HashSet<>(),
                new HashSet<>(), new HashSet<>(), new HashSet<>());
        User child1 = new User(2L, "test@test.com", "password", "1234567",
                "TestChild1", null, new HashSet<>(), new HashSet<>(), new HashSet<>(),
                new HashSet<>(), new HashSet<>(), new HashSet<>());
        User child2 = new User(3L, "test@test.com", "password", "1234567",
                "TestChild2", null, new HashSet<>(), new HashSet<>(), new HashSet<>(),
                new HashSet<>(), new HashSet<>(), new HashSet<>());

        ParentChildRelationship parentChildRelationship1 = new ParentChildRelationship(1L, parent, child1);
        child1.setParentRelationship(parentChildRelationship1);
        parent.getChildrenRelationships().add(parentChildRelationship1);

        ParentChildRelationship parentChildRelationship2 = new ParentChildRelationship(2L, parent, child1);
        child2.setParentRelationship(parentChildRelationship2);
        parent.getChildrenRelationships().add(parentChildRelationship2);
        when(userRepository.findChildrenByParentId(1L)).thenReturn(Arrays.asList(child1, child2));

        List<User> children = userService.getParentsChildren(parent);
        assertEquals(2, children.size());
        assertEquals("TestChild1", children.get(0).getFullName());
        assertEquals("TestChild2", children.get(1).getFullName());
    }

    @Test
    public void shouldReturnAllChildren_whenGetAllChildrenIsCalled() {
        User parent = new User(1L, "test@test.com", "password", "1234567",
                "TestParent", null, new HashSet<>(), new HashSet<>(), new HashSet<>(),
                new HashSet<>(), new HashSet<>(), new HashSet<>());
        User child1 = new User(2L, "test@test.com", "password", "1234567",
                "TestChild1", null, new HashSet<>(), new HashSet<>(), new HashSet<>(),
                new HashSet<>(), new HashSet<>(), new HashSet<>());
        User child2 = new User(3L, "test@test.com", "password", "1234567",
                "TestChild2", null, new HashSet<>(), new HashSet<>(), new HashSet<>(),
                new HashSet<>(), new HashSet<>(), new HashSet<>());

        ParentChildRelationship parentChildRelationship1 = new ParentChildRelationship(1L, parent, child1);
        child1.setParentRelationship(parentChildRelationship1);
        parent.getChildrenRelationships().add(parentChildRelationship1);

        ParentChildRelationship parentChildRelationship2 = new ParentChildRelationship(2L, parent, child1);
        child2.setParentRelationship(parentChildRelationship2);
        parent.getChildrenRelationships().add(parentChildRelationship2);
        when(userRepository.findUsersWithParents()).thenReturn(Arrays.asList(child1, child2));

        List<User> children = userService.getAllChildren();
        assertEquals(2, children.size());
        assertEquals("TestChild1", children.get(0).getFullName());
        assertEquals("TestChild2", children.get(1).getFullName());
    }
}
