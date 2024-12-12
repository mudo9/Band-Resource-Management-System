package uk.ac.sheffield.bandproject.Service;

import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.*;
import uk.ac.sheffield.bandproject.Model.*;
import uk.ac.sheffield.bandproject.Model.Band;
import uk.ac.sheffield.bandproject.Repository.BandRepository;
import uk.ac.sheffield.bandproject.Repository.UserRepository;
import uk.ac.sheffield.bandproject.Repository.ParentChildRelationshipRepository;
import java.util.*;


@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final BandRepository bandRepository;
    private final ParentChildRelationshipRepository parentChildRepository;

    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder, BandRepository bandRepository,
                           ParentChildRelationshipRepository parentChildRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.bandRepository = bandRepository;
        this.parentChildRepository = parentChildRepository;
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User register(User user) throws Exception {
        if(userRepository.findByEmailAndNoParent(user.getEmail()).isPresent()){
            throw new Exception("Email already exists: " + user.getEmail());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.getRoles().add(Role.MEMBER);
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> getAllParents() {
        return userRepository.findUsersWithParents();
    }

    public List<User> getUsersByBand(String bandName) {
        return userRepository.findByBandName(bandName);
    }

    public User addBandToUser(String userEmail, Long BandId) {
        Optional<User> userOpt = userRepository.findByEmailAndNoParent(userEmail);
        Optional<Band> bandOpt = bandRepository.findById(BandId);

        if(userOpt.isPresent() && bandOpt.isPresent()){
            User user = userOpt.get();
            Band band = bandOpt.get();

            user.getBands().add(band);
            return userRepository.save(user);
        }
        else{
            throw new RuntimeException("User not found");
        }
    }

    public User addBandToUserByFullName(String fullName, Long BandId) {
        Optional<User> userOptional = userRepository.findByFullName(fullName);
        Optional<Band> bandOptional = bandRepository.findById(BandId);

        if(userOptional.isPresent() && bandOptional.isPresent()){
            User user = userOptional.get();
            Band band = bandOptional.get();
            user.getBands().add(band);
            return userRepository.save(user);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public void deleteBandMember(Long userId, Long bandId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            Band band = bandRepository.findById(bandId)
                    .orElseThrow(() -> new IllegalArgumentException("Band not found"));
            if (user.getBands() != null && user.getBands().contains(band)) {
                Set<Band> userBands = user.getBands();
                userBands.remove(band);
                user.setBands(userBands);
                userRepository.save(user);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error removing user from band: " + e.getMessage());
        }
    }

    public List<User> getCommitteeMembers() {
        return userRepository.findByRole(Role.COMMITTEE_MEMBER);
    }

    public User promoteUserToCommitteeMember(String email) {
        Optional<User> userOptional = userRepository.findByEmailAndNoParent(email);
        if(userOptional.isPresent()){
            User user = userOptional.get();
            user.getRoles().add(Role.COMMITTEE_MEMBER);
            return userRepository.save(user);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public void demoteUserFromCommitteeMember(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if(userOptional.isPresent()){
            User user = userOptional.get();
            user.getRoles().remove(Role.COMMITTEE_MEMBER);
            userRepository.save(user);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmailAndNoParent(email);
    }

    public void addParentToChild(User child, User parent) {
        // Validate input
        if (child == null || parent == null) {
            throw new IllegalArgumentException("Parent or child cannot be null.");
        }

        parent.getRoles().add(Role.PARENT);
        // Ensure that the parent is persisted (saved)
        parent = userRepository.save(parent); // Save the parent if it's not already saved
        child.getRoles().add(Role.CHILD);
        // Ensure that the child is persisted (saved)
        child = userRepository.save(child); // Save the child if it's not already saved


        // Check if the child already has a parent assigned
        if (child.getParentRelationship() != null) {
            throw new IllegalStateException("This child already has a parent assigned.");
        }

        // Create the ParentChildRelationship entity and set the parent-child relationship
        ParentChildRelationship relationship = new ParentChildRelationship();
        relationship.setParent(parent);
        relationship.setChild(child);

        // Add the relationship to both parent and child
        parent.getChildrenRelationships().add(relationship);
        child.setParentRelationship(relationship);

        // Save the relationship
        parentChildRepository.save(relationship);
    }

    public List<User> getParentsChildren(User parent){
        return userRepository.findChildrenByParentId(parent.getId());
    }
    public List<User> getAllChildren(){
        return userRepository.findUsersWithParents();
    }

    public User updateAccount(Long id, User updatedUser){
        Optional<User> userOpt = userRepository.findById(id);
        if(userOpt.isPresent()){
            User user = userOpt.get();
            user.setFullName(updatedUser.getFullName());
            user.setEmail(updatedUser.getEmail());
            user.setPhoneNumber(updatedUser.getPhoneNumber());
            user.setPassword(updatedUser.getPassword());
            return userRepository.save(user);
        }else{
            throw new RuntimeException("User not found");
        }

    }


}
