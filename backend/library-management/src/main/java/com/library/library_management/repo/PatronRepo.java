package com.library.library_management.repo;

import com.library.library_management.model.Patron;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatronRepo extends JpaRepository<Patron , Long> {
    Patron save(Patron patron);

    Optional<Patron> findById(Long id);

    boolean existsById(Long id);

    void deleteById(Long id);
}
