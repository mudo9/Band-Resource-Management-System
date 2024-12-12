package uk.ac.sheffield.bandproject.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "music_orders") // Maps this entity to the "music_orders" table in the database
public class MusicOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Automatically generates unique ID values
    private Long id;

    @ManyToOne // Many-to-One relationship: multiple orders can be linked to a single owner
    @JoinColumn(name = "owner_id", nullable = false) // Foreign key column for the owner, cannot be null
    private User owner; // The adult owner (parent or independent member)

    @ManyToOne // Many-to-One relationship: multiple orders can be linked to a single child
    @JoinColumn(name = "child_id") // Foreign key column for the child, optional
    private User child; // Optional: The child on behalf of whom the order is made

    @NotNull(message = "Date is required") // Ensures the date field is mandatory
    private LocalDate date; // The date the music order was created

    @NotBlank(message = "Status is required") // Ensures the status field is mandatory and non-empty
    private String status; // The status of the order (e.g., pending, fulfilled, etc.)

    @ManyToMany // Many-to-Many relationship: an order can include multiple music parts
    @JoinTable(
            name = "music_part_orders", // Join table name in the database
            joinColumns = @JoinColumn(name = "music_order_id"), // Foreign key to this table
            inverseJoinColumns = @JoinColumn(name = "music_part_id") // Foreign key to the music parts table
    )
    private Set<MusicPart> musicParts = new HashSet<>(); // The music parts included in the order

    // Constructors

    public MusicOrder() {} // Default constructor

    public MusicOrder(Long id, User owner, User child, LocalDate date, String status, Set<MusicPart> musicParts) {
        this.id = id;
        this.owner = owner; // Assigns the owner of the music order
        this.child = child; // Assigns the child for whom the order is made (if any)
        this.date = date; // Sets the date the order was created
        this.status = status; // Sets the status of the order
        this.musicParts = musicParts; // Assigns the music parts included in the order
    }

    // Getters and Setters
    // Provide controlled access and modification for private fields

    public Long getId() {
        return id; // Returns the unique ID of the music order
    }
    public void setId(Long id) {
        this.id = id; // Sets the unique ID of the music order
    }
    public User getOwner() {
        return owner; // Returns the owner of the music order
    }
    public void setOwner(User owner) {
        this.owner = owner; // Sets the owner of the music order
    }
    public User getChild() {
        return child; // Returns the child associated with the music order
    }
    public void setChild(User child) {
        this.child = child; // Sets the child associated with the music order
    }
    public LocalDate getDate() {
        return date; // Returns the date of the music order
    }
    public void setDate(LocalDate date) {
        this.date = date; // Sets the date of the music order
    }
    public String getStatus() {
        return status; // Returns the status of the music order
    }
    public void setStatus(String status) {
        this.status = status; // Sets the status of the music order
    }
    public Set<MusicPart> getMusicParts() {
        return musicParts; // Returns the music parts included in the order
    }
    public void setMusicParts(Set<MusicPart> musicParts) {
        this.musicParts = musicParts; // Sets the music parts included in the order
    }
}
