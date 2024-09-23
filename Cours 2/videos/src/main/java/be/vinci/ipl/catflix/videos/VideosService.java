package be.vinci.ipl.catflix.videos;

import org.springframework.stereotype.Service;

@Service
public class VideosService {

    private final VideosRepository repository;

    public VideosService(VideosRepository repository) {
        this.repository = repository;
    }


    /**
     * Creates a video in repository
     * @param video the video to create
     * @return true if the video was created, or false if another video exists with same hash
     */
    public boolean createOne(Video video) {
        if (repository.existsById(video.getHash())) return false;
        repository.save(video);
        return true;
    }


    /**
     * Reads all videos in repository
     * @return all videos
     */
    public Iterable<Video> readAll() {
        return repository.findAll();
    }

    /**
     * Reads a video with a certain hash from repository
     * @param hash the hash to search for
     * @return the video, or null if the video couldn't be found
     */
    public Video readOne(String hash) {
        return repository.findById(hash).orElse(null);
    }

    /**
     * Reads all videos from an author
     * @param author the author of the videos
     * @return all videos from this author
     */
    public Iterable<Video> readFromAuthor(String author) {
        return repository.findByAuthor(author);
    }


    /**
     * Updates a video in repository
     * @param video the new values of the video
     * @return true if the video was updated, or false if the video couldn't be found
     */
    public boolean updateOne(Video video) {
        if (!repository.existsById(video.getHash())) return false;
        repository.save(video);
        return true;
    }


    /**
     * Deletes all videos from repository
     */
    public void deleteAll() {
        repository.deleteAll();
    }

    /**
     * Deletes a video with a certain hash from repository
     * @param hash the hash of the video
     * @return true if the video was deleted, or false if the video couldn't be found
     */
    public boolean deleteOne(String hash) {
        if (!repository.existsById(hash)) return false;
        repository.deleteById(hash);
        return true;
    }

    /**
     * Deletes all videos from an author
     * @param author the author of the videos
     */
    public void deleteFromAuthor(String author) {
        repository.deleteByAuthor(author);
    }

}
