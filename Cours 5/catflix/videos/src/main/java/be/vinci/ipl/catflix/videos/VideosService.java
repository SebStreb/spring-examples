package be.vinci.ipl.catflix.videos;

import be.vinci.ipl.catflix.videos.models.Video;
import be.vinci.ipl.catflix.videos.repositories.ReviewsProxy;
import be.vinci.ipl.catflix.videos.repositories.UsersProxy;
import be.vinci.ipl.catflix.videos.repositories.VideosRepository;
import feign.FeignException;
import org.springframework.stereotype.Service;

@Service
public class VideosService {

    private final VideosRepository repository;
    private final ReviewsProxy reviewsProxy;
    private final UsersProxy usersProxy;

    public VideosService(VideosRepository repository, ReviewsProxy reviewsProxy, UsersProxy usersProxy) {
        this.repository = repository;
        this.reviewsProxy = reviewsProxy;
        this.usersProxy = usersProxy;
    }

    /**
     * Checks if a user exists in the users service
     * @param pseudo Pseudo of the user
     * @return true if the user does not exist, false otherwise
     */
    public boolean userNotExists(String pseudo) {
        try {
            usersProxy.readOne(pseudo);
            return false;
        } catch (FeignException.FeignClientException e) {
            if (e.status() == 404) return true;
            else throw e;
        }
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
     * Deletes all videos from repository and all reviews associated with them
     */
    public void deleteAll() {
        for (Video video : repository.findAll()) reviewsProxy.deleteFromVideo(video.getHash());

        repository.deleteAll();
    }

    /**
     * Deletes a video with a certain hash from repository and all reviews associated with it
     * @param hash the hash of the video
     * @return true if the video was deleted, or false if the video couldn't be found
     */
    public boolean deleteOne(String hash) {
        if (!repository.existsById(hash)) return false;

        reviewsProxy.deleteFromVideo(hash);

        repository.deleteById(hash);
        return true;
    }

    /**
     * Deletes all videos from an author and all reviews associated with them
     * @param author the author of the videos
     */
    public void deleteFromAuthor(String author) {
        for (Video video : repository.findByAuthor(author)) reviewsProxy.deleteFromVideo(video.getHash());

        repository.deleteByAuthor(author);
    }

}
