package be.vinci.ipl.catflix.gateway.data;

import be.vinci.ipl.catflix.gateway.models.Credentials;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

@Repository
@FeignClient(name = "authentication")
public interface AuthenticationProxy {

    @PostMapping("/authentication/connect")
    String connect(@RequestBody Credentials credentials);

    @PostMapping("/authentication/verify")
    String verify(@RequestBody String token);

}
