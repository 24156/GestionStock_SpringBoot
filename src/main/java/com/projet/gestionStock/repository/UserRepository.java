package com.projet.gestionStock.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.projet.gestionStock.model.User;
import java.util.Optional;



public interface UserRepository extends JpaRepository<User,Long>{

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}