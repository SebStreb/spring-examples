package be.vinci.ipl.catflix.reviews;

import be.vinci.ipl.catflix.reviews.models.Review;
import be.vinci.ipl.catflix.reviews.models.Video;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@RestController
public class ReviewsController {

    private final ReviewsService service;

    public ReviewsController(ReviewsService service) {
        this.service = service;
    }

    @PostMapping("/reviews/users/{pseudo}/videos/{hash}")
    public ResponseEntity<Void> createOne(@PathVariable String pseudo, @PathVariable String hash, @RequestBody Review review) {
        if (!Objects.equals(review.getPseudo(), pseudo) || !Objects.equals(review.getHash(), hash)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if (review.invalid()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        if (service.userNotExists(pseudo) || service.videoNotExists(hash)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        boolean created = service.createOne(review);
        if (!created) throw new ResponseStatusException(HttpStatus.CONFLICT);
        else return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @GetMapping("/reviews/users/{pseudo}/videos/{hash}")
    public Review readOne(@PathVariable String pseudo, @PathVariable String hash) {
        Review review = service.readOne(pseudo, hash);

        if (review == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        else return review;
    }

    @GetMapping("/reviews/users/{pseudo}")
    public Iterable<Review> readFromUser(@PathVariable String pseudo) {
        return service.readFromUser(pseudo);
    }

    @GetMapping("/reviews/videos/{hash}")
    public Iterable<Review> readFromVideo(@PathVariable String hash) {
        return service.readFromVideo(hash);
    }

    @GetMapping("/reviews/best")
    public Iterable<Video> readBest() {
        return service.best3Videos();
    }


    @PutMapping("/reviews/users/{pseudo}/videos/{hash}")
    public void updateOne(@PathVariable String pseudo, @PathVariable String hash, @RequestBody Review review) {
        if (!Objects.equals(review.getPseudo(), pseudo) || !Objects.equals(review.getHash(), hash)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if (review.invalid()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        if (service.userNotExists(pseudo) || service.videoNotExists(hash)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        boolean updated = service.updateOne(review);
        if (!updated) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }


    @DeleteMapping("/reviews/users/{pseudo}/videos/{hash}")
    public void deleteOne(@PathVariable String pseudo, @PathVariable String hash) {
        boolean deleted = service.deleteOne(pseudo, hash);
        if (!deleted) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/reviews/users/{pseudo}")
    public void deleteFromUser(@PathVariable String pseudo) {
        service.deleteFromUser(pseudo);
    }

    @DeleteMapping("/reviews/videos/{hash}")
    public void deleteFromVideo(@PathVariable String hash) {
        service.deleteFromVideo(hash);
    }

}
