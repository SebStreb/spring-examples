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
     * @return true if the video was created, false if another video exists with same hash
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
     * Updates a video in repository
     * @param newVideo the new values of the video
     * @return the old values of the video, or null if the video couldn't be found
     */
    public Video updateOne(Video newVideo) {
        Video oldVideo = readOne(newVideo.getHash());
        if (oldVideo == null) return null;
        repository.save(newVideo);
        return oldVideo;
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
     * @return the old values of the video, or null if the video couldn't be found
     */
    public Video deleteOne(String hash) {
        Video oldVideo = readOne(hash);
        if (oldVideo == null) return null;
        repository.deleteById(hash);
        return oldVideo;
    }

}
