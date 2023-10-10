package be.vinci.ipl.catflix.gateway.data;

import be.vinci.ipl.catflix.gateway.models.Review;
import be.vinci.ipl.catflix.gateway.models.Video;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

@Repository
@FeignClient(name = "reviews")
public interface ReviewsProxy {

    @PostMapping("/reviews/{pseudo}/{hash}")
    void createReview(@PathVariable String pseudo, @PathVariable String hash, @RequestBody Review review);

    @GetMapping("/reviews/{pseudo}/{hash}")
    Review readReview(@PathVariable String pseudo, @PathVariable String hash);

    @PutMapping("/reviews/{pseudo}/{hash}")
    void updateReview(@PathVariable String pseudo, @PathVariable String hash, @RequestBody Review review);

    @DeleteMapping("/reviews/{pseudo}/{hash}")
    void deleteReview(@PathVariable String pseudo, @PathVariable String hash);


    @GetMapping("/reviews/user/{pseudo}")
    Iterable<Review> readReviewsFromUser(@PathVariable String pseudo);

    @DeleteMapping("/reviews/user/{pseudo}")
    void deleteReviewsFromUser(@PathVariable String pseudo);


    @GetMapping("/reviews/video/{hash}")
    Iterable<Review> readReviewsOfVideo(@PathVariable String hash);

    @DeleteMapping("/reviews/video/{hash}")
    void deleteReviewsOfVideo(@PathVariable String hash);


    @GetMapping("/reviews/best")
    Iterable<Video> readBestVideos();

}
