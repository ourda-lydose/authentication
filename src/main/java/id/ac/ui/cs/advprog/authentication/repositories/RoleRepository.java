package id.ac.ui.cs.advprog.authentication.repositories;

import id.ac.ui.cs.advprog.authentication.entities.ERole;
import id.ac.ui.cs.advprog.authentication.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
