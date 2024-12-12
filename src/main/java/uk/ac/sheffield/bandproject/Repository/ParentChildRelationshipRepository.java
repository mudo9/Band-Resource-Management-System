package uk.ac.sheffield.bandproject.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.ac.sheffield.bandproject.Model.ParentChildRelationship;
import uk.ac.sheffield.bandproject.Model.User;

import java.util.*;

public interface ParentChildRelationshipRepository extends JpaRepository<ParentChildRelationship, Long>{
    List<ParentChildRelationship> findByParent(User parent);
    Optional<ParentChildRelationship> findByChild(User child);
}
