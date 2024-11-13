package be.vinci.ipl.catflix.mockreviews;

import be.vinci.ipl.catflix.mockreviews.models.Review;
import be.vinci.ipl.catflix.mockreviews.models.Video;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class ReviewsController {

    @PostMapping("/reviews/users/{pseudo}/videos/{hash}")
    public ResponseEntity<Void> createOne(@PathVariable String pseudo, @PathVariable String hash, @RequestBody Review review) {
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @GetMapping("/reviews/users/{pseudo}/videos/{hash}")
    public Review readOne(@PathVariable String pseudo, @PathVariable String hash) {
        Review review = new Review();
        review.setPseudo(pseudo);
        review.setHash(hash);
        review.setRating(5);
        review.setComment("Great movie!");
        return review;
    }

    @GetMapping("/reviews/users/{pseudo}")
    public Iterable<Review> readFromUser(@PathVariable String pseudo) {
        return new ArrayList<>();
    }

    @GetMapping("/reviews/videos/{hash}")
    public Iterable<Review> readFromVideo(@PathVariable String hash) {
        return new ArrayList<>();
    }

    @GetMapping("/reviews/best")
    public Iterable<Video> readBest() {
        ArrayList<Video> videos = new ArrayList<>();

        Video video1 = new Video();
        video1.setHash("hash1");
        video1.setName("Video 1");
        video1.setAuthor("Author 1");
        video1.setCreationYear(2020);
        video1.setDuration(120);
        video1.setUrl("https://www.youtube.com/watch?v=hash1");
        videos.add(video1);

        Video video2 = new Video();
        video2.setHash("hash2");
        video2.setName("Video 2");
        video2.setAuthor("Author 2");
        video2.setCreationYear(2021);
        video2.setDuration(150);
        video2.setUrl("https://www.youtube.com/watch?v=hash2");
        videos.add(video2);

        Video video3 = new Video();
        video3.setHash("hash3");
        video3.setName("Video 3");
        video3.setAuthor("Author 3");
        video3.setCreationYear(2019);
        video3.setDuration(180);
        video3.setUrl("https://www.youtube.com/watch?v=hash3");
        videos.add(video3);

        return videos;
    }


    @PutMapping("/reviews/users/{pseudo}/videos/{hash}")
    public void updateOne(@PathVariable String pseudo, @PathVariable String hash, @RequestBody Review review) {
    }


    @DeleteMapping("/reviews/users/{pseudo}/videos/{hash}")
    public void deleteOne(@PathVariable String pseudo, @PathVariable String hash) {
    }

    @DeleteMapping("/reviews/users/{pseudo}")
    public void deleteFromUser(@PathVariable String pseudo) {
    }

    @DeleteMapping("/reviews/videos/{hash}")
    public void deleteFromVideo(@PathVariable String hash) {
    }

}
