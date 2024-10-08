package be.vinci.ipl.catflix.gateway.data;

import be.vinci.ipl.catflix.gateway.models.User;
import be.vinci.ipl.catflix.gateway.models.UserWithCredentials;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

@Repository
@FeignClient(name = "users")
public interface UsersProxy {

    @PostMapping("/users/{pseudo}")
    void createUser(@PathVariable String pseudo, @RequestBody UserWithCredentials user);

    @GetMapping("/users/{pseudo}")
    User readUser(@PathVariable String pseudo);

    @PutMapping("/users/{pseudo}")
    void updateUser(@PathVariable String pseudo, @RequestBody UserWithCredentials user);

    @DeleteMapping("/users/{pseudo}")
    void deleteUser(@PathVariable String pseudo);

}
