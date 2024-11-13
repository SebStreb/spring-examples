package be.vinci.ipl.catflix.mockusers;

import be.vinci.ipl.catflix.mockusers.models.User;
import be.vinci.ipl.catflix.mockusers.models.UserWithCredentials;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UsersController {

    @PostMapping("/users/{pseudo}")
    public ResponseEntity<Void> createOne(@PathVariable String pseudo, @RequestBody UserWithCredentials user) {
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/users/{pseudo}")
    public User readOne(@PathVariable String pseudo) {
        User user = new User();
        user.setPseudo(pseudo);
        user.setFirstname("test");
        user.setLastname("test");
        return user;
    }

    @PutMapping("/users/{pseudo}")
    public void updateOne(@PathVariable String pseudo, @RequestBody UserWithCredentials user) {
    }

    @DeleteMapping("/users/{pseudo}")
    public void deleteOne(@PathVariable String pseudo) {
    }

}
