package be.vinci.ipl.catflix.users;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UsersController {

    private final UsersService service;

    public UsersController(UsersService service) {
        this.service = service;
    }

    @PostMapping("/users/{pseudo}")
    public ResponseEntity<Void> createOne(@PathVariable String pseudo, @RequestBody User user) {
        if (!user.getPseudo().equals(pseudo)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        boolean created = service.createOne(user);
        if (!created) return new ResponseEntity<>(HttpStatus.CONFLICT);
        else return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/users/{pseudo}")
    public ResponseEntity<User> readOne(@PathVariable String pseudo) {
        User user = service.readOne(pseudo);
        if (user == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/users/{pseudo}")
    public ResponseEntity<User> updateOne(@PathVariable String pseudo, @RequestBody User user) {
        if (!user.getPseudo().equals(pseudo)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        boolean updated = service.updateOne(user);
        if (updated) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/users/{pseudo}")
    public ResponseEntity<User> deleteOne(@PathVariable String pseudo) {
        boolean deleted = service.deleteOne(pseudo);
        if (deleted) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else return new ResponseEntity<>(HttpStatus.OK);
    }

}
