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

    /**
     * Create a video
     * @request POST /videos
     * @body video to create
     * @response 209: video already exists, 201: returns create video
     */
    @PostMapping("/videos")
    public ResponseEntity<Video> createOne(@RequestBody Video video) {
        boolean created = service.createOne(video);
        if (!created) return new ResponseEntity<>(HttpStatus.CONFLICT);
        else return new ResponseEntity<>(video, HttpStatus.CREATED);
    }

    /**
     * Read all video
     * @request GET /videos
     * @response 200: returns all videos
     */
    @GetMapping("/videos")
    public Iterable<Video> readAll() {
        return service.readAll();
    }

    /**
     * Read a video
     * @request GET /videos/{hash}
     * @response 404: video not found, 200: returns found video
     */
    @GetMapping("/videos/{hash}")
    public ResponseEntity<Video> readOne(@PathVariable String hash) {
        Video video = service.readOne(hash);
        if (video == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else return new ResponseEntity<>(video, HttpStatus.OK);
    }

    /**
     * Update a video
     * @request PUT /videos/{hash}
     * @body new value of the video
     * @response 400: video does not match hash, 404: video not found, 200: returns old value of video
     */
    @PutMapping("/videos/{hash}")
    public ResponseEntity<Video> updateOne(@PathVariable String hash, @RequestBody Video video) {
        if (!video.getHash().equals(hash)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Video oldVideo = service.updateOne(video);
        if (oldVideo == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else return new ResponseEntity<>(oldVideo, HttpStatus.OK);
    }

    /**
     * Delete all videos
     * @request DELETE /videos
     * @response 200: all videos are deleted
     */
    @DeleteMapping("/videos")
    public void deleteAll() {
        service.deleteAll();
    }

    /**
     * Delete a video
     * @request  DELETE /video/{hash}
     * @response 404: video not found, 200: returns deleted video
     */
    @DeleteMapping("/videos/{hash}")
    public ResponseEntity<Video> deleteOne(@PathVariable String hash) {
        Video video = service.deleteOne(hash);
        if (video == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else return new ResponseEntity<>(video, HttpStatus.OK);
    }

}
