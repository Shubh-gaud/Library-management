package com.library.library_management.Controller;

import com.library.library_management.Service.PatronService;
import com.library.library_management.model.Patron;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/patrons")
@CrossOrigin
public class PatronController {

    private final PatronService patronService;

    @Autowired
    public PatronController(PatronService patronService) {
        this.patronService = patronService;
    }

    @PostMapping
    public ResponseEntity<Patron> createPatron(@RequestBody Patron patron){
        Patron savedPatron = patronService.savePatron(patron);
        return new ResponseEntity<>(savedPatron, HttpStatus.CREATED);
    }

    @GetMapping
    public List<Patron> getAllPatrons(){
        return patronService.getAllPatrons();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Patron> getPatronById(@PathVariable Long id) {
        Optional<Patron> optionalPatron = patronService.getPatronById(id);

        if (optionalPatron.isPresent()) {
            Patron patron = optionalPatron.get();
            return new ResponseEntity<>(patron, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<Patron> updatePatron(@PathVariable Long id, @RequestBody Patron patronDetails) {
        return patronService.getPatronById(id).map(existingPatron -> {
            existingPatron.setFirstName(patronDetails.getFirstName());
            existingPatron.setLastName(patronDetails.getLastName());
            existingPatron.setEmail(patronDetails.getEmail());
            existingPatron.setPhoneNumber(patronDetails.getPhoneNumber());
            existingPatron.setMembershipStatus(patronDetails.getMembershipStatus());

            Patron updatedPatron = patronService.savePatron(existingPatron);
            return new ResponseEntity<>(updatedPatron, HttpStatus.OK);
        }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatron(@PathVariable Long id){
        if(patronService.deletePatron(id)){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
