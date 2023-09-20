package be.vinci.ipl.catflix.videos;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class VideosController {

    private static final List<Video> videos = new ArrayList<>();

    static {
        videos.add(new Video(
                "JxS5E-kZc2s",
                "Funny Cats Compilation (Most Popular) Part 1",
                "NoCAT NoLiFE 2",
                2015,
                1004,
                "https://www.youtube.com/watch?v=JxS5E-kZc2s"
        ));
        videos.add(new Video(
                "ZuRLOlB4N8U",
                "Cute Animals for When You are Stressed",
                "PetWard",
                2021,
                949,
                "https://www.youtube.com/watch?v=ZuRLOlB4N8U"
        ));
        videos.add(new Video(
                "dQw4w9WgXcQ",
                "Cutest kitty ever",
                "Cat4Life",
                2009,
                212,
                "https://www.youtube.com/watch?v=dQw4w9WgXcQ"
        ));
    }

    /**
     * Find the index in the list of videos of the video with a certain hash
     * @param hash the hash to search for
     * @return the index of the video with this hash, or -1 if none is found
     */
    private int findIndex(String hash) {
        return videos.stream().map(Video::getHash).toList().indexOf(hash);
    }

    /**
     * Check if a video with a certain hash exists
     * @param hash the hash to search for
     * @return true if the video with this hash exists, false otherwise
     */
    private boolean exists(String hash) {
        return findIndex(hash) != -1;
    }


    /**
     * Create a video
     * @request POST /videos
     * @body video to create
     * @response 409: video already exists, 201: returns created video
     */
    @PostMapping("/videos")
    public ResponseEntity<Video> createOne(@RequestBody Video video) {
        if (exists(video.getHash())) return new ResponseEntity<>(HttpStatus.CONFLICT);

        videos.add(video);
        return new ResponseEntity<>(video, HttpStatus.CREATED);
    }

    /**
     * Read all videos
     * @request GET /videos
     * @response 200: returns all videos
     */
    @GetMapping("/videos")
    public Iterable<Video> readAll() {
        return videos;
    }

    /**
     * Read a video
     * @request GET /videos/{hash}
     * @response 404: video not found, 200: returns found video
     */
    @GetMapping("/videos/{hash}")
    public ResponseEntity<Video> readOne(@PathVariable String hash) {
        Video video = videos.stream().filter(it -> it.getHash().equals(hash)).findFirst().orElse(null);

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

        int index = findIndex(hash);
        if (index == -1) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Video oldVideo = videos.set(index, video);
        return new ResponseEntity<>(oldVideo, HttpStatus.OK);
    }

    /**
     * Delete all videos
     * @request DELETE /videos
     * @response 200: all videos are deleted
     */
    @DeleteMapping("/videos")
    public void deleteAll() {
        videos.clear();
    }

    /**
     * Delete a video
     * @request  DELETE /video/{hash}
     * @response 404: video not found, 200: returns deleted video
     */
    @DeleteMapping("/videos/{hash}")
    public ResponseEntity<Video> deleteOne(@PathVariable String hash) {
        int index = findIndex(hash);
        if (index == -1) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Video video = videos.remove(index);
        return new ResponseEntity<>(video, HttpStatus.OK);
    }

}
