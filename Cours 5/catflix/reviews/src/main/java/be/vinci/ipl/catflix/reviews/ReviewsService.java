package be.vinci.ipl.catflix.reviews;

import be.vinci.ipl.catflix.reviews.models.Review;
import be.vinci.ipl.catflix.reviews.models.Video;
import be.vinci.ipl.catflix.reviews.repositories.ReviewsRepository;
import be.vinci.ipl.catflix.reviews.repositories.UsersProxy;
import be.vinci.ipl.catflix.reviews.repositories.VideosProxy;
import feign.FeignException;
import jakarta.persistence.Tuple;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.stream.StreamSupport;

@Service
public class ReviewsService {

    private final ReviewsRepository repository;
    private final VideosProxy videosProxy;
    private final UsersProxy usersProxy;

    public ReviewsService(ReviewsRepository repository, VideosProxy videosProxy, UsersProxy usersProxy) {
        this.repository = repository;
        this.videosProxy = videosProxy;
        this.usersProxy = usersProxy;
    }

    /**
     * Checks if a user exists in the users service
     * @param pseudo Pseudo of the user
     * @return true if the user exists, false otherwise
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
     * Checks if a video exists in the videos service
     * @param hash Hash of the video
     * @return true if the video exists, false otherwise
     */
    public boolean videoNotExists(String hash) {
        try {
            videosProxy.readOne(hash);
            return false;
        } catch (FeignException.FeignClientException e) {
            if (e.status() == 404) return true;
            else throw e;
        }
    }


    /**
     * Creates a review in repository
     * @param review Review to create
     * @return true if the review was created, false if another review exists with the same pseudo and hash
     */
    public boolean createOne(Review review) {
        if (repository.existsByPseudoAndHash(review.getPseudo(), review.getHash())) return false;
        repository.save(review);
        return true;
    }


    /**
     * Reads a review in repository
     * @param pseudo Pseudo of the user reviewing
     * @param hash Hash of the video being reviewed
     * @return The review, or null if the review couldn't be found
     */
    public Review readOne(String pseudo, String hash) {
        return repository.findByPseudoAndHash(pseudo, hash).orElse(null);
    }

    /**
     * Reads all reviews from a user
     * @param pseudo Pseudo of the user
     * @return The list of reviews from this user
     */
    public Iterable<Review> readFromUser(String pseudo) {
        return repository.findByPseudo(pseudo);
    }

    /**
     * Reads all reviews of a video
     * @param hash Hash of the video
     * @return The list of reviews of this video
     */
    public Iterable<Review> readFromVideo(String hash) {
        return repository.findByHash(hash);
    }

    /**
     * Finds the 3 best videos by average ranking of users
     * @return the list of videos
     */
    public Iterable<Video> best3Videos() {
        Iterable<Tuple> bests = repository.findBest();
        return StreamSupport.stream(bests.spliterator(), false)
                .limit(3)
                .map(best -> {
                    try {
                        String hash = (String) best.get("videoHash");
                        return videosProxy.readOne(hash);
                    } catch (FeignException.FeignClientException e) {
                        System.err.println("Consistency error: no such hash: " + best.get("videoHash"));
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();
    }


    /**
     * Updates a review in repository
     * @param newReview New values of the review
     * @return true if the review was updated, or false if the review couldn't be found
     */
    public boolean updateOne(Review newReview) {
        Review oldReview = repository.findByPseudoAndHash(newReview.getPseudo(), newReview.getHash()).orElse(null);
        if (oldReview == null) return false;

        newReview.setId(oldReview.getId());
        repository.save(newReview);
        return true;
    }


    /**
     * Deletes a review from repository
     * @param pseudo Pseudo of the user reviewing
     * @param hash   Hash of the video being reviewed
     * @return true if the review was deleted, or false if the review couldn't be found
     */
    public boolean deleteOne(String pseudo, String hash) {
        if (!repository.existsByPseudoAndHash(pseudo, hash)) return false;
        repository.deleteByPseudoAndHash(pseudo, hash);
        return true;
    }

    /**
     * Deletes all reviews from a user
     * @param pseudo Pseudo of the user
     */
    public void deleteFromUser(String pseudo) {
        repository.deleteByPseudo(pseudo);
    }

    /**
     * Deletes all reviews of a video
     * @param hash Hash of the video
     */
    public void deleteFromVideo(String hash) {
        repository.deleteByHash(hash);
    }

}
