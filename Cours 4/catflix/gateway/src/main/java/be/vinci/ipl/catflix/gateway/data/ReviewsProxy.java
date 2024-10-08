package be.vinci.ipl.catflix.gateway.data;

import be.vinci.ipl.catflix.gateway.models.Review;
import be.vinci.ipl.catflix.gateway.models.Video;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

@Repository
@FeignClient(name = "reviews")
public interface ReviewsProxy {

    @PostMapping("/reviews/users/{pseudo}/videos/{hash}")
    void createReview(@PathVariable String pseudo, @PathVariable String hash, @RequestBody Review review);


    @GetMapping("/reviews/users/{pseudo}/videos/{hash}")
    Review readReview(@PathVariable String pseudo, @PathVariable String hash);

    @GetMapping("/reviews/users/{pseudo}")
    Iterable<Review> readReviewsFromUser(@PathVariable String pseudo);

    @GetMapping("/reviews/videos/{hash}")
    Iterable<Review> readReviewsOfVideo(@PathVariable String hash);

    @GetMapping("/reviews/best")
    Iterable<Video> readBestVideos();


    @PutMapping("/reviews/users/{pseudo}/videos/{hash}")
    void updateReview(@PathVariable String pseudo, @PathVariable String hash, @RequestBody Review review);


    @DeleteMapping("/reviews/users/{pseudo}/videos/{hash}")
    void deleteReview(@PathVariable String pseudo, @PathVariable String hash);

}
