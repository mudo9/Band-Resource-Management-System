package uk.ac.sheffield.bandproject.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.HashSet;
import java.util.Set;

// Declares this class as a JPA entity mapped to the "users" table in the database
@Entity
@Table(name = "users")
public class User {

    // Marks 'id' as the primary key for this entity with auto-generated values
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Stores the user's email, with validation to ensure it is in a valid email format
    @Email(message = "Email should be valid")
    private String email;

    // Stores the user's password
    private String password;

    // Stores the user's phone number
    private String phoneNumber;

    // Stores the user's full name
    private String fullName;

    // Defines a one-to-many relationship for parent-child relationships where this user is a parent
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ParentChildRelationship> childrenRelationships = new HashSet<>();

    // Defines a one-to-one relationship where this user is a child
    @OneToOne(mappedBy = "child", cascade = CascadeType.ALL)
    private ParentChildRelationship parentRelationship;

    // Stores the roles assigned to the user as a collection of enum values, fetched eagerly
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();

    // Many-to-many relationship for bands the user is associated with
    @ManyToMany
    @JoinTable(
            name = "band_member", // Specifies the join table name
            joinColumns = @JoinColumn(name = "user_id"), // Foreign key for User
            inverseJoinColumns = @JoinColumn(name = "band_id") // Foreign key for Band
    )
    private Set<Band> bands = new HashSet<>();

    // One-to-many relationship for music orders placed by the user
    @OneToMany(mappedBy = "owner")
    private Set<MusicOrder> musicOrders = new HashSet<>();

    // One-to-many relationship for instrument loans associated with the user
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<InstrumentLoan> instrumentLoans = new HashSet<>();

    // One-to-many relationship for miscellaneous loans associated with the user
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MiscellaneousLoan> miscellaneousLoans = new HashSet<>();

    // Default constructor required by JPA
    public User() {}

    // Constructor to initialize all fields
    public User(Long id, String email, String password, String phone, String fullName,
                ParentChildRelationship parentRelationship, Set<Role> roles,
                Set<Band> bands, Set<InstrumentLoan> instrumentLoans, Set<MiscellaneousLoan> miscellaneousLoans,
                Set<MusicOrder> musicOrders, Set<ParentChildRelationship> childrenRelationships) {
        this.id = id;
        this.email = email; // Sets the user's email
        this.password = password; // Sets the user's password
        this.phoneNumber = phone; // Sets the user's phone number
        this.fullName = fullName; // Sets the user's full name
        this.parentRelationship = parentRelationship; // Links the user to a parent relationship
        this.roles = roles; // Sets the user's roles
        this.bands = bands; // Associates the user with bands
        this.instrumentLoans = instrumentLoans; // Links the user to their instrument loans
        this.miscellaneousLoans = miscellaneousLoans; // Links the user to their miscellaneous loans
        this.musicOrders = musicOrders; // Links the user to their music orders
        this.childrenRelationships = childrenRelationships; // Links the user to their children relationships
    }

    // Getters and setters for accessing and modifying the entity's fields

    public Long getId() {
        return id; // Returns the user's ID
    }

    public void setId(Long id) {
        this.id = id; // Updates the user's ID
    }

    public String getFullName() {
        return fullName; // Returns the user's full name
    }

    public void setFullName(String fullName) {
        this.fullName = fullName; // Updates the user's full name
    }

    public String getEmail() {
        return email; // Returns the user's email
    }

    public void setEmail(String email) {
        this.email = email; // Updates the user's email
    }

    public String getPassword() {
        return password; // Returns the user's password
    }

    public void setPassword(String password) {
        this.password = password; // Updates the user's password
    }

    public String getPhoneNumber() {
        return phoneNumber; // Returns the user's phone number
    }

    public void setPhoneNumber(String phone) {
        this.phoneNumber = phone; // Updates the user's phone number
    }

    public Set<Role> getRoles() {
        return roles; // Returns the user's roles
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles; // Updates the user's roles
    }

    public Set<Band> getBands() {
        return bands; // Returns the bands associated with the user
    }

    public void setBands(Set<Band> bands) {
        this.bands = bands; // Updates the bands associated with the user
    }

    public Set<InstrumentLoan> getInstrumentLoans() {
        return instrumentLoans; // Returns the instrument loans associated with the user
    }

    public void setInstrumentLoans(Set<InstrumentLoan> instrumentLoans) {
        this.instrumentLoans = instrumentLoans; // Updates the instrument loans associated with the user
    }

    public Set<MiscellaneousLoan> getMiscellaneousLoans() {
        return miscellaneousLoans; // Returns the miscellaneous loans associated with the user
    }

    public void setMiscellaneousLoans(Set<MiscellaneousLoan> miscellaneousLoans) {
        this.miscellaneousLoans = miscellaneousLoans; // Updates the miscellaneous loans associated with the user
    }

    public Set<MusicOrder> getMusicOrders() {
        return musicOrders; // Returns the music orders associated with the user
    }

    public void setMusicOrders(Set<MusicOrder> musicOrders) {
        this.musicOrders = musicOrders; // Updates the music orders associated with the user
    }

    public Set<ParentChildRelationship> getChildrenRelationships() {
        return childrenRelationships; // Returns the parent-child relationships where the user is a parent
    }

    public void setChildrenRelationships(Set<ParentChildRelationship> childrenRelationships) {
        this.childrenRelationships = childrenRelationships; // Updates the parent-child relationships
    }

    public ParentChildRelationship getParentRelationship() {
        return parentRelationship; // Returns the parent-child relationship where the user is a child
    }

    public void setParentRelationship(ParentChildRelationship parentRelationship) {
        this.parentRelationship = parentRelationship; // Updates the parent-child relationship where the user is a child
    }

    // Dynamic accessors for inherited fields

    // Returns the email of the parent if the user has a parent relationship, otherwise returns the user's email
    public String getEffectiveEmail() {
        if (parentRelationship != null) {
            return parentRelationship.getParent().getEmail();
        }
        return email;
    }

    // Returns the phone number of the parent if the user has a parent relationship, otherwise returns the user's phone number
    public String getEffectivePhoneNumber() {
        if (parentRelationship != null) {
            return parentRelationship.getParent().getPhoneNumber();
        }
        return phoneNumber;
    }

    // Returns the password of the parent if the user has a parent relationship, otherwise returns the user's password
    public String getEffectivePassword() {
        if (parentRelationship != null) {
            return parentRelationship.getParent().getPassword();
        }
        return password;
    }
}

