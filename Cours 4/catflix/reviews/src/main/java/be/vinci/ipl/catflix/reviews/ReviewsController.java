package be.vinci.ipl.catflix.reviews;

import be.vinci.ipl.catflix.reviews.models.Review;
import be.vinci.ipl.catflix.reviews.models.Video;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
public class ReviewsController {

    private final ReviewsService service;

    public ReviewsController(ReviewsService service) {
        this.service = service;
    }

    @PostMapping("/reviews/{pseudo}/{hash}")
    public ResponseEntity<Void> createOne(@PathVariable String pseudo, @PathVariable String hash, @RequestBody Review review) {
        if (!Objects.equals(review.getPseudo(), pseudo) || !Objects.equals(review.getHash(), hash)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (review.invalid()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        boolean created = service.createOne(review);

        if (!created) return new ResponseEntity<>(HttpStatus.CONFLICT);
        else return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/reviews/{pseudo}/{hash}")
    public ResponseEntity<Review> readOne(@PathVariable String pseudo, @PathVariable String hash) {
        Review review = service.readOne(pseudo, hash);

        if (review == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else return new ResponseEntity<>(review, HttpStatus.OK);
    }

    @PutMapping("/reviews/{pseudo}/{hash}")
    public ResponseEntity<Void> updateOne(@PathVariable String pseudo, @PathVariable String hash, @RequestBody Review review) {
        if (!Objects.equals(review.getPseudo(), pseudo) || !Objects.equals(review.getHash(), hash)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (review.invalid()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        boolean updated = service.updateOne(review);

        if (!updated) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/reviews/{pseudo}/{hash}")
    public ResponseEntity<Void>  deleteOne(@PathVariable String pseudo, @PathVariable String hash) {
        boolean deleted = service.deleteOne(pseudo, hash);

        if (!deleted) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/reviews/user/{pseudo}")
    public Iterable<Review> readFromUser(@PathVariable String pseudo) {
        return service.readFromUser(pseudo);
    }

    @DeleteMapping("/reviews/user/{pseudo}")
    public void deleteFromUser(@PathVariable String pseudo) {
        service.deleteFromUser(pseudo);
    }


    @GetMapping("/reviews/video/{hash}")
    public Iterable<Review> readFromVideo(@PathVariable String hash) {
        return service.readFromVideo(hash);
    }

    @DeleteMapping("/reviews/video/{hash}")
    public void deleteFromVideo(@PathVariable String hash) {
        service.deleteFromVideo(hash);
    }


    @GetMapping("/reviews/best")
    public Iterable<Video> readBest() {
        return service.best3Videos();
    }

}
