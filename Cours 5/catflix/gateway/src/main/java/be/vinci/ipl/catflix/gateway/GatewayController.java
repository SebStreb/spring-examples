package be.vinci.ipl.catflix.gateway;

import be.vinci.ipl.catflix.gateway.models.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@RestController
public class GatewayController {

    private final GatewayService service;

    public GatewayController(GatewayService service) {
        this.service = service;
    }


    @PostMapping("/auth")
    public String connect(@RequestBody Credentials credentials) {
        return service.connect(credentials); // throws BadRequestException & UnauthorizedException
    }


    @PostMapping("/users/{pseudo}")
    public ResponseEntity<Void> createUser(@PathVariable String pseudo, @RequestBody UserWithCredentials user) {
        if (!Objects.equals(user.getPseudo(), pseudo)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        service.createUser(user); // throws BadRequestException & ConflictException
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/users/{pseudo}")
    public User readUser(@PathVariable String pseudo) {
        return service.readUser(pseudo); // throws NotFoundException
    }

    @PutMapping("/users/{pseudo}")
    public void updateUser(@PathVariable String pseudo, @RequestBody UserWithCredentials user, @RequestHeader("Authorization") String token) {
        if (!Objects.equals(user.getPseudo(), pseudo)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        service.verify(token, pseudo); // throws UnauthorizedException & ForbiddenException

        service.updateUser(user); // throws BadRequestException & NotFoundException
    }

    @DeleteMapping("/users/{pseudo}")
    public void deleteUser(@PathVariable String pseudo, @RequestHeader("Authorization") String token) {
        service.verify(token, pseudo); // throws UnauthorizedException & ForbiddenException

        service.deleteUser(pseudo); // throws NotFoundException
    }


    @GetMapping("/users/{pseudo}/videos")
    public Iterable<Video> readUserVideos(@PathVariable String pseudo) {
        return service.readVideosFromUser(pseudo);
    }

    @GetMapping("/users/{pseudo}/reviews")
    public Iterable<Review> readUserReviews(@PathVariable String pseudo) {
        return service.readReviewsFromUser(pseudo);
    }


    @GetMapping("/videos")
    public Iterable<Video> readVideos() {
        return service.readVideos();
    }


    @GetMapping("/videos/best")
    public Iterable<Video> readBestVideos() {
        return service.readBestVideos();
    }


    @PostMapping("/videos/{hash}")
    public ResponseEntity<Void> createVideo(@PathVariable String hash, @RequestBody Video video, @RequestHeader("Authorization") String token) {
        if (!Objects.equals(video.getHash(), hash)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        service.verify(token, video.getAuthor()); // throws UnauthorizedException & ForbiddenException

        service.createVideo(video); // throws BadRequestException & ConflictException
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/videos/{hash}")
    public Video readVideo(@PathVariable String hash) {
        return service.readVideo(hash); // throws NotFoundException
    }

    @PutMapping("/videos/{hash}")
    public void updateVideo(@PathVariable String hash, @RequestBody Video video, @RequestHeader("Authorization") String token) {
        if (!Objects.equals(video.getHash(), hash)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        service.verify(token, video.getAuthor()); // throws UnauthorizedException & ForbiddenException

        service.updateVideo(video); // throws BadRequestException & NotFoundException
    }

    @DeleteMapping("/videos/{hash}")
    public void deleteVideo(@PathVariable String hash, @RequestHeader("Authorization") String token) {
        Video video = service.readVideo(hash); // throws NotFoundException
        service.verify(token, video.getAuthor()); // throws UnauthorizedException & ForbiddenException

        service.deleteVideo(hash); // throws NotFoundException
    }


    @GetMapping("/videos/{hash}/reviews")
    public Iterable<Review> readVideoReviews(@PathVariable String hash) {
        return service.readReviewsOfVideo(hash);
    }


    @PostMapping("/reviews/users/{pseudo}/videos/{hash}")
    public ResponseEntity<Void> createReview(@PathVariable String pseudo, @PathVariable String hash, @RequestBody Review review, @RequestHeader("Authorization") String token) {
        if (!Objects.equals(review.getPseudo(), pseudo) || !Objects.equals(review.getHash(), hash)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        service.verify(token, pseudo); // throws UnauthorizedException & ForbiddenException

        service.createReview(review); // throws BadRequestException & ConflictException
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/reviews/users/{pseudo}/videos/{hash}")
    public Review readReview(@PathVariable String pseudo, @PathVariable String hash) {
        return service.readReview(pseudo, hash); // throws NotFoundException
    }

    @PutMapping("/reviews/users/{pseudo}/videos/{hash}")
    public void updateReview(@PathVariable String pseudo, @PathVariable String hash, @RequestBody Review review, @RequestHeader("Authorization") String token) {
        if (!Objects.equals(review.getPseudo(), pseudo) || !Objects.equals(review.getHash(), hash)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        service.verify(token, pseudo); // throws UnauthorizedException & ForbiddenException

        service.updateReview(review); // throws BadRequestException & NotFoundException
    }

    @DeleteMapping("/reviews/users/{pseudo}/videos/{hash}")
    public void deleteReview(@PathVariable String pseudo, @PathVariable String hash, @RequestHeader("Authorization") String token) {
        service.verify(token, pseudo); // throws UnauthorizedException & ForbiddenException

        service.deleteReview(pseudo, hash); // throws NotFoundException
    }

}
