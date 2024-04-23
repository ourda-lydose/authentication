package id.ac.ui.cs.advprog.authentication.repositories;

import id.ac.ui.cs.advprog.authentication.entities.User;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    Optional<User> findByEmail(String email);
}