package uk.ac.sheffield.bandproject.Service;

import uk.ac.sheffield.bandproject.Model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> getUserById(Long id);
    User register(User user) throws Exception;
    List<User> getAllUsers();
    List<User> getAllParents();
    List<User> getUsersByBand(String bandName);
    User addBandToUser(String userEmail, Long BandId);
    User addBandToUserByFullName(String fullName, Long BandId);
    void deleteBandMember(Long userId, Long bandId);
    List<User> getCommitteeMembers();
    User promoteUserToCommitteeMember(String email);
    void demoteUserFromCommitteeMember(Long id);
    Optional<User> getUserByEmail(String email);
    void addParentToChild(User child, User parent);
    List<User> getParentsChildren(User parent);
    List<User> getAllChildren();
    User updateAccount(Long id, User updatedUser);



}
