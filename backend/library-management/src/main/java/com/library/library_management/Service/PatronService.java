package com.library.library_management.Service;

import com.library.library_management.model.Patron;
import com.library.library_management.repo.PatronRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatronService {

    private final PatronRepo patronRepo;

    @Autowired
    public PatronService(PatronRepo patronRepo) {
        this.patronRepo = patronRepo;
    }

    public Patron savePatron(Patron patron) {
        return patronRepo.save(patron);
    }

    public List<Patron> getAllPatrons() {
        return patronRepo.findAll();
    }

    public Optional<Patron> getPatronById(Long id){
        return patronRepo.findById(id);
    }


    public boolean deletePatron(Long id) {
        if(patronRepo.existsById(id)){
            patronRepo.deleteById(id);
            return true;
        }
        return false;
    }
}
