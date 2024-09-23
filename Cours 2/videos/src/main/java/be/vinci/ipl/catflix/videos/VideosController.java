package be.vinci.ipl.catflix.videos;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@RestController
public class VideosController {

    private final VideosService service;

    public VideosController(VideosService service) {
        this.service = service;
    }


    @PostMapping("/videos/{hash}")
    public ResponseEntity<Void> createOne(@PathVariable String hash, @RequestBody Video video) {
        if (!Objects.equals(video.getHash(), hash)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        if (video.invalid()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        boolean created = service.createOne(video);

        if (!created) throw new ResponseStatusException(HttpStatus.CONFLICT);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @GetMapping("/videos")
    public Iterable<Video> readAll() {
        return service.readAll();
    }

    @GetMapping("/videos/{hash}")
    public Video readOne(@PathVariable String hash) {
        Video video = service.readOne(hash);

        if (video == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        else return video;
    }

    @GetMapping("/videos/user/{author}")
    public Iterable<Video> readFromAuthor(@PathVariable String author) {
        return service.readFromAuthor(author);
    }


    @PutMapping("/videos/{hash}")
    public void updateOne(@PathVariable String hash, @RequestBody Video video) {
        if (!Objects.equals(video.getHash(), hash)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        if (video.invalid()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        boolean found = service.updateOne(video);
        if (!found) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }


    @DeleteMapping("/videos")
    public void deleteAll() {
        service.deleteAll();
    }

    @DeleteMapping("/videos/{hash}")
    public void deleteOne(@PathVariable String hash) {
        boolean found = service.deleteOne(hash);
        if (!found) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/videos/user/{author}")
    public void deleteFromAuthor(@PathVariable String author) {
        service.deleteFromAuthor(author);
    }

}
