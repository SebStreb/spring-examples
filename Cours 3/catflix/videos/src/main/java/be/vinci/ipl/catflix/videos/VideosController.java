package be.vinci.ipl.catflix.videos;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class VideosController {

    private final VideosService service;

    public VideosController(VideosService service) {
        this.service = service;
    }

    @PostMapping("/videos")
    public ResponseEntity<Void> createOne(@RequestBody Video video) {
        boolean created = service.createOne(video);
        if (!created) return new ResponseEntity<>(HttpStatus.CONFLICT);
        else return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/videos")
    public Iterable<Video> readAll() {
        return service.readAll();
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
        boolean updated = service.updateOne(video);
        if (!updated) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/videos")
    public void deleteAll() {
        service.deleteAll();
    }

    @DeleteMapping("/videos/{hash}")
    public ResponseEntity<Video> deleteOne(@PathVariable String hash) {
        boolean deleted = service.deleteOne(hash);
        if (!deleted) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else return new ResponseEntity<>(HttpStatus.OK);
    }

}
