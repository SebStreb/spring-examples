package be.vinci.ipl.catflix.mockauthentication;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthenticationController {

    @PostMapping("/authentication/connect")
    public String connect(@RequestBody Credentials credentials) {
        return "JWT token";
    }


    @PostMapping("/authentication/verify")
    public String verify(@RequestBody String token) {
        return "pseudo";
    }


    @PostMapping("/authentication/{pseudo}")
    public ResponseEntity<Void> createOne(@PathVariable String pseudo, @RequestBody Credentials credentials) {
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/authentication/{pseudo}")
    public void updateOne(@PathVariable String pseudo, @RequestBody Credentials credentials) {
    }

    @DeleteMapping("/authentication/{pseudo}")
    public void deleteCredentials(@PathVariable String pseudo) {
    }

}
