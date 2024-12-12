package uk.ac.sheffield.bandproject.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "music_parts") // Maps this entity to the "music_parts" table in the database
public class MusicPart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Automatically generates unique ID values
    private Long id;

    @NotBlank(message = "Part name is required") // Ensures the part name is not null or empty
    private String partName; // The name of the music part

    @ManyToOne(fetch = FetchType.LAZY) // Many-to-One relationship: multiple music parts can belong to one music set
    @JoinColumn(name = "music_set_id", nullable = false) // Foreign key column linking to the music set, cannot be null
    private MusicSet musicSet; // The music set this part belongs to

    @ManyToMany(mappedBy = "musicParts", cascade = CascadeType.ALL) // Many-to-Many relationship with MusicOrder, mapped by "musicParts" in MusicOrder
    private Set<MusicOrder> musicOrders = new HashSet<>(); // The orders that include this music part

    // Constructors

    public MusicPart() {} // Default constructor

    public MusicPart(Long id, String partName, MusicSet musicSet, Set<MusicOrder> musicOrders) {
        this.id = id;
        this.partName = partName; // Assigns the name of the music part
        this.musicSet = musicSet; // Links the part to its associated music set
        this.musicOrders = musicOrders; // Assigns the orders that include this music part
    }

    // Getters and Setters
    // Provide controlled access and modification for private fields

    public Long getId() {
        return id; // Returns the unique ID of the music part
    }
    public void setId(Long id) {
        this.id = id; // Sets the unique ID of the music part
    }
    public String getPartName() {
        return partName; // Returns the name of the music part
    }
    public void setPartName(String partName) {
        this.partName = partName; // Sets the name of the music part
    }
    public MusicSet getMusicSet() {
        return musicSet; // Returns the music set this part belongs to
    }
    public void setMusicSet(MusicSet musicSet) {
        this.musicSet = musicSet; // Sets the music set this part belongs to
    }
    public Set<MusicOrder> getMusicOrders() {
        return musicOrders; // Returns the orders that include this music part
    }
    public void setMusicOrders(Set<MusicOrder> musicOrders) {
        this.musicOrders = musicOrders; // Sets the orders that include this music part
    }
}
