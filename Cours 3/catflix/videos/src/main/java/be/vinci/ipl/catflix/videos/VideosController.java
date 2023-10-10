package be.vinci.ipl.catflix.videos;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
public class VideosController {

    private final VideosService service;

    public VideosController(VideosService service) {
        this.service = service;
    }

    @GetMapping("/videos")
    public Iterable<Video> readAll() {
        return service.readAll();
    }

    @DeleteMapping("/videos")
    public void deleteAll() {
        service.deleteAll();
    }

    @PostMapping("/videos/{hash}")
    public ResponseEntity<Video> createOne(@PathVariable String hash, @RequestBody Video video) {
        if (!Objects.equals(video.getHash(), hash)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (video.invalid()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        boolean created = service.createOne(video);

        if (!created) return new ResponseEntity<>(HttpStatus.CONFLICT);
        else return new ResponseEntity<>(video, HttpStatus.CREATED);
    }

    @GetMapping("/videos/{hash}")
    public ResponseEntity<Video> readOne(@PathVariable String hash) {
        Video video = service.readOne(hash);

        if (video == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else return new ResponseEntity<>(video, HttpStatus.OK);
    }

    @PutMapping("/videos/{hash}")
    public ResponseEntity<Video> updateOne(@PathVariable String hash, @RequestBody Video video) {
        if (!video.getHash().equals(hash)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (video.invalid()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        boolean found = service.updateOne(video);

        if (!found) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/videos/{hash}")
    public ResponseEntity<Video> deleteOne(@PathVariable String hash) {
        boolean found = service.deleteOne(hash);

        if (!found) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/videos/user/{author}")
    public Iterable<Video> readFromAuthor(@PathVariable String author) {
        return service.readFromAuthor(author);
    }

    @DeleteMapping("/videos/user/{author}")
    public void deleteFromAuthor(@PathVariable String author) {
        service.deleteFromAuthor(author);
    }

}
