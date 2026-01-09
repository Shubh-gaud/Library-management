package com.library.library_management.repo;

import com.library.library_management.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepo  extends JpaRepository<Book,Long>{

    Book findByIsbn(String isbn);

    Optional<Book> findById(Long id);

    void deleteById(Long id);

    boolean existsById(Long id);
}
