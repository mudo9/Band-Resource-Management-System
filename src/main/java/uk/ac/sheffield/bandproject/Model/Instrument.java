package uk.ac.sheffield.bandproject.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "instruments") // Specifies the database table name for the Instrument entity
public class Instrument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Automatically generates unique ID values
    private Long id;

    @NotBlank // Ensures the serial number is not null or empty
    private String serialNumber;

    @NotBlank // Ensures the name is not null or empty
    private String name;

    @NotBlank // Ensures the make is not null or empty
    private String make;

    @OneToMany(mappedBy = "instrument", cascade = CascadeType.ALL, orphanRemoval = true)
    // Defines a one-to-many relationship with InstrumentLoan, where Instrument is the parent
    private Set<InstrumentLoan> instrumentLoan = new HashSet<>();

    @OneToMany(mappedBy = "instrument", cascade = CascadeType.ALL, orphanRemoval = true)
    // Defines a one-to-many relationship with InstrumentNote, where Instrument is the parent
    private Set<InstrumentNote> instrumentNote = new HashSet<>();

    // Constructors
    public Instrument() {} // Default constructor

    public Instrument(Long id, String serialNumber, String name, String make, Set<InstrumentLoan> instrumentLoan,
                      Set<InstrumentNote> instrumentNote) {
        this.id = id;
        this.serialNumber = serialNumber; // Assigns the serial number of the instrument
        this.name = name; // Assigns the name of the instrument
        this.make = make; // Assigns the make of the instrument
        this.instrumentLoan = instrumentLoan; // Assigns the associated instrument loans
        this.instrumentNote = instrumentNote; // Assigns the associated instrument notes
    }

    // Getters and Setters
    // Provide controlled access and modification for private fields

    public Long getId() {
        return id; // Returns the unique ID of the instrument
    }
    public void setId(Long id) {
        this.id = id; // Sets the unique ID of the instrument
    }
    public String getSerialNumber() {
        return serialNumber; // Returns the serial number of the instrument
    }
    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber; // Sets the serial number of the instrument
    }
    public String getName() {
        return name; // Returns the name of the instrument
    }
    public void setName(String name) {
        this.name = name; // Sets the name of the instrument
    }
    public String getMake() {
        return make; // Returns the make of the instrument
    }
    public void setMake(String make) {
        this.make = make; // Sets the make of the instrument
    }
    public Set<InstrumentLoan> getInstrumentLoan() {
        return instrumentLoan; // Returns the set of instrument loans associated with the instrument
    }
    public void setInstrumentLoan(Set<InstrumentLoan> instrumentLoan) {
        this.instrumentLoan = instrumentLoan; // Sets the instrument loans associated with the instrument
    }
    public Set<InstrumentNote> getInstrumentNote() {
        return instrumentNote; // Returns the set of instrument notes associated with the instrument
    }
    public void setInstrumentNote(Set<InstrumentNote> instrumentNote) {
        this.instrumentNote = instrumentNote; // Sets the instrument notes associated with the instrument
    }
}
