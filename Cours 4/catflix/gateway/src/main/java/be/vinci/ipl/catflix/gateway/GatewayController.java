package be.vinci.ipl.catflix.gateway;

import be.vinci.ipl.catflix.gateway.exceptions.BadRequestException;
import be.vinci.ipl.catflix.gateway.exceptions.ConflictException;
import be.vinci.ipl.catflix.gateway.exceptions.NotFoundException;
import be.vinci.ipl.catflix.gateway.exceptions.UnauthorizedException;
import be.vinci.ipl.catflix.gateway.models.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
public class GatewayController {

    private final GatewayService service;

    public GatewayController(GatewayService service) {
        this.service = service;
    }


    @PostMapping("/auth")
    public ResponseEntity<String> connect(@RequestBody Credentials credentials) {
        try {
            String token = service.connect(credentials);
            return new ResponseEntity<>(token, HttpStatus.OK);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }


    @PostMapping("/users/{pseudo}")
    public ResponseEntity<Void> createUser(@PathVariable String pseudo, @RequestBody UserWithCredentials user) {
        if (!Objects.equals(user.getPseudo(), pseudo)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        try {
            service.createUser(user);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (ConflictException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/users/{pseudo}")
    public ResponseEntity<User> readUser(@PathVariable String pseudo) {
        User user = service.readUser(pseudo);

        if (user == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/users/{pseudo}")
    public ResponseEntity<Void> updateUser(@PathVariable String pseudo,
                                           @RequestBody UserWithCredentials user,
                                           @RequestHeader("Authorization") String token) {
        if (!Objects.equals(user.getPseudo(), pseudo)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        String userPseudo = service.verify(token);
        if (userPseudo == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        else if (!Objects.equals(userPseudo, pseudo)) return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        try {
            service.updateUser(user);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/users/{pseudo}")
    public ResponseEntity<Void> deleteUser(@PathVariable String pseudo, @RequestHeader("Authorization") String token) {
        String user = service.verify(token);
        if (user == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        else if (!Objects.equals(user, pseudo)) return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        boolean found = service.deleteUser(pseudo);

        if (!found) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping("/users/{pseudo}/videos")
    public ResponseEntity<Iterable<Video>> readUserVideos(@PathVariable String pseudo) {
        Iterable<Video> videos = service.readVideosFromUser(pseudo);

        if (videos == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else return new ResponseEntity<>(videos, HttpStatus.OK);
    }

    @GetMapping("/users/{pseudo}/reviews")
    public ResponseEntity<Iterable<Review>> readUserReviews(@PathVariable String pseudo) {
        Iterable<Review> reviews = service.readReviewsFromUser(pseudo);

        if (reviews == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else return new ResponseEntity<>(reviews, HttpStatus.OK);
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
    public ResponseEntity<Void> createVideo(@PathVariable String hash,
                                            @RequestBody Video video,
                                            @RequestHeader("Authorization") String token) {
        if (!Objects.equals(video.getHash(), hash)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        String user = service.verify(token);
        if (user == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        else if (!Objects.equals(video.getAuthor(), user)) return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        try {
            service.createVideo(video);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (ConflictException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

    }

    @GetMapping("/videos/{hash}")
    public ResponseEntity<Video> readVideo(@PathVariable String hash) {
        Video video = service.readVideo(hash);

        if (video == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else return new ResponseEntity<>(video, HttpStatus.OK);
    }

    @PutMapping("/videos/{hash}")
    public ResponseEntity<Void> updateVideo(@PathVariable String hash,
                                            @RequestBody Video video,
                                            @RequestHeader("Authorization") String token) {
        if (!Objects.equals(video.getHash(), hash)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        String user = service.verify(token);
        if (user == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        else if (!Objects.equals(video.getAuthor(), user)) return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        try {
            service.updateVideo(video);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/videos/{hash}")
    public ResponseEntity<Void> deleteVideo(@PathVariable String hash, @RequestHeader("Authorization") String token) {
        Video video = service.readVideo(hash);
        if (video == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        String user = service.verify(token);
        if (user == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        else if (!Objects.equals(video.getAuthor(), user)) return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        boolean found = service.deleteVideo(hash);

        if (!found) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping("/videos/{hash}/reviews")
    public ResponseEntity<Iterable<Review>> readVideoReviews(@PathVariable String hash) {
        Iterable<Review> reviews = service.readReviewsOfVideo(hash);

        if (reviews == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else return new ResponseEntity<>(reviews, HttpStatus.OK);
    }


    @PostMapping("/reviews/{pseudo}/{hash}")
    public ResponseEntity<Void> createReview(@PathVariable String pseudo,
                                             @PathVariable String hash,
                                             @RequestBody Review review,
                                             @RequestHeader("Authorization") String token) {
        if (!Objects.equals(review.getPseudo(), pseudo) || !Objects.equals(review.getHash(), hash)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        String user = service.verify(token);
        if (user == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        else if (!Objects.equals(review.getPseudo(), user)) return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        try {
            service.createReview(review);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (ConflictException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/reviews/{pseudo}/{hash}")
    public ResponseEntity<Review> readReview(@PathVariable String pseudo, @PathVariable String hash) {
        Review review = service.readReview(pseudo, hash);

        if (review == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else return new ResponseEntity<>(review, HttpStatus.OK);
    }

    @PutMapping("/reviews/{pseudo}/{hash}")
    public ResponseEntity<Void> updateReview(@PathVariable String pseudo,
                                             @PathVariable String hash,
                                             @RequestBody Review review,
                                             @RequestHeader("Authorization") String token) {
        if (!Objects.equals(review.getPseudo(), pseudo) || !Objects.equals(review.getHash(), hash)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        String user = service.verify(token);
        if (user == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        else if (!Objects.equals(review.getPseudo(), user)) return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        try {
            service.updateReview(review);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/reviews/{pseudo}/{hash}")
    public ResponseEntity<Void> deleteReview(@PathVariable String pseudo,
                                             @PathVariable String hash,
                                             @RequestHeader("Authorization") String token) {
        Review review = service.readReview(pseudo, hash);
        if (review == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        String user = service.verify(token);
        if (user == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        else if (!review.getPseudo().equals(user)) return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        boolean found = service.deleteReview(pseudo, hash);

        if (!found) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else return new ResponseEntity<>(HttpStatus.OK);
    }

}
