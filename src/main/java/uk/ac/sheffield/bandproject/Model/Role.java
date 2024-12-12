package uk.ac.sheffield.bandproject.Model;

// Enum to define the various roles a user can have within the system
public enum Role {
    // Represents a user with the role of Director, likely responsible for overseeing operations
    DIRECTOR,

    // Represents a user who is a member of a committee, involved in decision-making processes
    COMMITTEE_MEMBER,

    // Represents a standard member role, typically with fewer responsibilities
    MEMBER,

    // Represents a user classified as a child in the parent-child relationship
    CHILD,

    // Represents a user classified as a parent in the parent-child relationship
    PARENT
}
