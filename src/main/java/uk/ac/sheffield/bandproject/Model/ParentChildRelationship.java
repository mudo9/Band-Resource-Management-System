package uk.ac.sheffield.bandproject.Model;

import jakarta.persistence.*;

// Declares this class as a JPA entity mapped to the "parent_child_relationship" table in the database
@Entity
@Table(name = "parent_child_relationship")
public class ParentChildRelationship {

    // Marks 'id' as the primary key for this entity, with auto-generated values
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many-to-one relationship indicating that multiple child-parent relationships can share the same parent
    @ManyToOne
    @JoinColumn(name = "parent_id", nullable = false) // Specifies the foreign key column for the parent
    private User parent;

    // One-to-one relationship indicating that each child can have only one unique parent
    @OneToOne
    @JoinColumn(name = "child_id", nullable = false, unique = true) // Specifies the foreign key column for the child
    private User child;

    // Default constructor required by JPA
    public ParentChildRelationship() {}

    // Constructor to initialize the relationship with a parent and a child
    public ParentChildRelationship(Long id, User parent, User child) {
        this.id = id;
        this.parent = parent;
        this.child = child;
    }

    // Getters and setters for accessing and modifying the entity's fields

    public Long getId() {
        return id; // Returns the ID of the relationship
    }

    public void setId(Long id) {
        this.id = id; // Sets the ID of the relationship
    }

    public User getParent() {
        return parent; // Returns the parent user in the relationship
    }

    public void setParent(User parent) {
        this.parent = parent; // Updates the parent user in the relationship
    }

    public User getChild() {
        return child; // Returns the child user in the relationship
    }

    public void setChild(User child) {
        this.child = child; // Updates the child user in the relationship
    }
}

