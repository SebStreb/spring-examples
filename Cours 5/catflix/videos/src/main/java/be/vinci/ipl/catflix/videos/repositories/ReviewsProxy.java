package be.vinci.ipl.catflix.videos.repositories;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Repository
@FeignClient(name = "reviews")
public interface ReviewsProxy {

    @DeleteMapping("/reviews/videos/{hash}")
    void deleteFromVideo(@PathVariable String hash);

}
