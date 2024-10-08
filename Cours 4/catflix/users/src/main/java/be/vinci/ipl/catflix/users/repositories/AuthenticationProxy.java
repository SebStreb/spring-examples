package be.vinci.ipl.catflix.users.repositories;

import be.vinci.ipl.catflix.users.models.Credentials;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

@Repository
@FeignClient(name = "authentication")
public interface AuthenticationProxy {

    @PostMapping("/authentication/{pseudo}")
    void createOne(@PathVariable String pseudo, @RequestBody Credentials credentials);

    @PutMapping("/authentication/{pseudo}")
    void updateOne(@PathVariable String pseudo, @RequestBody Credentials credentials);

    @DeleteMapping("/authentication/{pseudo}")
    void deleteOne(@PathVariable String pseudo);

}
