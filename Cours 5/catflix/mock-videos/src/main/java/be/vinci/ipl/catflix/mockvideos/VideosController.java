package be.vinci.ipl.catflix.mockvideos;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class VideosController {

    @PostMapping("/videos/{hash}")
    public ResponseEntity<Void> createOne(@PathVariable String hash, @RequestBody Video video) {
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @GetMapping("/videos")
    public Iterable<Video> readAll() {
        return new ArrayList<>();
    }

    @GetMapping("/videos/{hash}")
    public Video readOne(@PathVariable String hash) {
        Video video = new Video();
        video.setHash(hash);
        video.setName("Mock video");
        video.setAuthor("Mock author");
        video.setCreationYear(2021);
        video.setDuration(60);
        video.setUrl("https://www.youtube.com/watch?v=" + hash);
        return video;
    }

    @GetMapping("/videos/users/{author}")
    public Iterable<Video> readFromAuthor(@PathVariable String author) {
        return new ArrayList<>();
    }


    @PutMapping("/videos/{hash}")
    public void updateOne(@PathVariable String hash, @RequestBody Video video) {
    }


    @DeleteMapping("/videos")
    public void deleteAll() {
    }

    @DeleteMapping("/videos/{hash}")
    public void deleteOne(@PathVariable String hash) {
    }

    @DeleteMapping("/videos/users/{pseudo}")
    public void deleteFromAuthor(@PathVariable String pseudo) {
    }

}
