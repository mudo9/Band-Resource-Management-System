package uk.ac.sheffield.bandproject.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.HashSet;
import java.util.Set;

// Declares this class as a JPA entity mapped to the "miscellaneous" table in the database
@Entity
@Table(name = "miscellaneous")
public class Miscellaneous {

    // Marks 'id' as the primary key for this entity with auto-generated values
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Name of the miscellaneous item, cannot be null or blank
    @NotBlank
    private String name;

    // Make or brand of the miscellaneous item, cannot be null or blank
    @NotBlank
    private String make;

    // Quantity of the miscellaneous item available, cannot be null
    @NotNull
    private Integer quantity;

    // Optional field specifying if the item is specific to an instrument
    private String specificForInstrument;

    // One-to-many relationship linking this item to its associated loans, with cascading operations and orphan removal
    @OneToMany(mappedBy = "miscellaneous", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MiscellaneousLoan> miscellaneousLoan = new HashSet<>();

    // Default constructor required by JPA
    public Miscellaneous() {}

    // Constructor to initialize all fields
    public Miscellaneous(Long id, String name, String make, Integer quantity, String specificForInstrument,
                         Set<MiscellaneousLoan> miscellaneousLoan) {
        this.id = id;
        this.name = name; // Sets the name of the item
        this.make = make; // Sets the make or brand of the item
        this.quantity = quantity; // Sets the quantity of the item
        this.specificForInstrument = specificForInstrument; // Specifies if the item is for a specific instrument
        this.miscellaneousLoan = miscellaneousLoan; // Associates the item with its loans
    }

    // Getters and setters for accessing and modifying the entity's fields

    public Long getId() {
        return id; // Returns the ID of the miscellaneous item
    }
    public void setId(Long id) {
        this.id = id; // Updates the ID of the miscellaneous item
    }

    public String getName() {
        return name; // Returns the name of the miscellaneous item
    }
    public void setName(String name) {
        this.name = name; // Updates the name of the miscellaneous item
    }

    public String getMake() {
        return make; // Returns the make or brand of the miscellaneous item
    }
    public void setMake(String make) {
        this.make = make; // Updates the make or brand of the miscellaneous item
    }

    public Integer getQuantity() {
        return quantity; // Returns the quantity of the miscellaneous item
    }
    public void setQuantity(Integer quantity) {
        this.quantity = quantity; // Updates the quantity of the miscellaneous item
    }

    public String getSpecificForInstrument() {
        return specificForInstrument; // Returns the instrument specificity of the miscellaneous item
    }
    public void setSpecificForInstrument(String specificForInstrument) {
        this.specificForInstrument = specificForInstrument; // Updates the instrument specificity of the item
    }

    public Set<MiscellaneousLoan> getMiscellaneousLoan() {
        return miscellaneousLoan; // Returns the set of loans associated with this item
    }
    public void setMiscellaneousLoan(Set<MiscellaneousLoan> miscellaneousLoan) {
        this.miscellaneousLoan = miscellaneousLoan; // Updates the loans associated with this item
    }
}

