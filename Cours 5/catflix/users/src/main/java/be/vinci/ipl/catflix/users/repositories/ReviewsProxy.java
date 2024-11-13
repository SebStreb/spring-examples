package be.vinci.ipl.catflix.users.repositories;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Repository
@FeignClient(name = "reviews")
public interface ReviewsProxy {

    @DeleteMapping("/reviews/users/{pseudo}")
    void deleteFromUser(@PathVariable String pseudo);

}
