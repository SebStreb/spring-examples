package be.vinci.ipl.catflix.gateway;

import be.vinci.ipl.catflix.gateway.data.AuthenticationProxy;
import be.vinci.ipl.catflix.gateway.data.ReviewsProxy;
import be.vinci.ipl.catflix.gateway.data.UsersProxy;
import be.vinci.ipl.catflix.gateway.data.VideosProxy;
import be.vinci.ipl.catflix.gateway.exceptions.BadRequestException;
import be.vinci.ipl.catflix.gateway.exceptions.ConflictException;
import be.vinci.ipl.catflix.gateway.exceptions.NotFoundException;
import be.vinci.ipl.catflix.gateway.exceptions.UnauthorizedException;
import be.vinci.ipl.catflix.gateway.models.*;
import feign.FeignException;
import org.springframework.stereotype.Service;

@Service
public class GatewayService {

    private final AuthenticationProxy authenticationProxy;
    private final ReviewsProxy reviewsProxy;
    private final UsersProxy usersProxy;
    private final VideosProxy videosProxy;

    public GatewayService(AuthenticationProxy authenticationProxy,
                          ReviewsProxy reviewsProxy,
                          UsersProxy usersProxy,
                          VideosProxy videosProxy) {
        this.authenticationProxy = authenticationProxy;
        this.reviewsProxy = reviewsProxy;
        this.usersProxy = usersProxy;
        this.videosProxy = videosProxy;
    }

    /**
     * Get connection token from credentials
     *
     * @param credentials Credentials of the user
     * @return Connection token
     * @throws BadRequestException when the credentials are invalid
     * @throws UnauthorizedException when the credentials are incorrect
     */
    public String connect(Credentials credentials) throws BadRequestException, UnauthorizedException {
        try {
            return authenticationProxy.connect(credentials);
        } catch (FeignException e) {
            if (e.status() == 400) throw new BadRequestException();
            else if (e.status() == 401) throw new UnauthorizedException();
            else throw e;
        }
    }

    /**
     * Get user pseudo from connection token
     *
     * @param token Connection token
     * @return User pseudo, or null if token invalid
     */
    public String verify(String token) {
        try {
            return authenticationProxy.verify(token);
        } catch (FeignException e) {
            if (e.status() == 401) return null;
            else throw e;
        }
    }

    /**
     * Create user and credentials
     *
     * @param user User to create with credentials
     * @throws BadRequestException When the user or the credentials are not valid
     * @throws ConflictException   When the user or the credentials already exist
     */
    public void createUser(UserWithCredentials user) throws BadRequestException, ConflictException {
        try {
            usersProxy.createUser(user.getPseudo(), user.toUser());
        } catch (FeignException e) {
            if (e.status() == 400) throw new BadRequestException();
            else if (e.status() == 409) throw new ConflictException();
            else throw e;
        }

        try {
            authenticationProxy.createCredentials(user.getPseudo(), user.toCredentials());
        } catch (FeignException e) {
            try {
                usersProxy.deleteUser(user.getPseudo());
            } catch (FeignException ignored) {
            }

            if (e.status() == 400) throw new BadRequestException();
            else if (e.status() == 409) throw new ConflictException();
            else throw e;
        }
    }

    /**
     * Read user information
     *
     * @param pseudo Pseudo of the user
     * @return User information, or null if user not found
     */
    public User readUser(String pseudo) {
        try {
            return usersProxy.readUser(pseudo);
        } catch (FeignException e) {
            if (e.status() == 404) return null;
            else throw e;
        }
    }

    /**
     * Update user and credentials
     *
     * @param user User to create with credentials
     * @throws BadRequestException When the user or the credentials are not valid
     * @throws NotFoundException   When the user or the credentials couldn't be found
     */
    public void updateUser(UserWithCredentials user) throws BadRequestException, NotFoundException {
        User previousUser;
        try {
            previousUser = usersProxy.readUser(user.getPseudo());
        } catch (FeignException e) {
            if (e.status() == 404) throw new NotFoundException();
            else throw e;
        }

        try {
            usersProxy.updateUser(user.getPseudo(), user.toUser());
        } catch (FeignException e) {
            if (e.status() == 400) throw new BadRequestException();
            else if (e.status() == 404) throw new NotFoundException();
            else throw e;
        }

        try {
            authenticationProxy.updateCredentials(user.getPseudo(), user.toCredentials());
        } catch (FeignException e) {
            try {
                usersProxy.updateUser(user.getPseudo(), previousUser);
            } catch (FeignException ignored) {
            }

            if (e.status() == 400) throw new BadRequestException();
            else if (e.status() == 404) throw new NotFoundException();
            else throw e;
        }
    }

    /**
     * Delete user and everything linked to them
     *
     * @param pseudo Pseudo of the user
     * @return false if either no user or credentials found for this pseudo, true otherwise
     */
    public boolean deleteUser(String pseudo) {
        reviewsProxy.deleteReviewsFromUser(pseudo);
        videosProxy.deleteVideosFromAuthor(pseudo);

        boolean found = true;

        try {
            authenticationProxy.deleteCredentials(pseudo);
        } catch (FeignException e) {
            if (e.status() == 404) found = false;
            else throw e;
        }

        try {
            usersProxy.deleteUser(pseudo);
        } catch (FeignException e) {
            if (e.status() == 404) found = false;
            else throw e;
        }

        System.out.println(found);

        return found;
    }

    /**
     * Read all videos
     *
     * @return all videos
     */
    public Iterable<Video> readVideos() {
        return videosProxy.readVideos();
    }

    /**
     * Create a new video
     *
     * @param video the video to create
     * @throws BadRequestException when the video is not valid
     * @throws NotFoundException   when the author of the video does not exist
     * @throws ConflictException   when a video already exists for this hash
     */
    public void createVideo(Video video) throws BadRequestException, NotFoundException, ConflictException {
        try {
            usersProxy.readUser(video.getAuthor());
        } catch (FeignException e) {
            if (e.status() == 404) throw new NotFoundException();
            else throw e;
        }

        try {
            videosProxy.createVideo(video.getHash(), video);
        } catch (FeignException e) {
            if (e.status() == 400) throw new BadRequestException();
            else if (e.status() == 409) throw new ConflictException();
            else throw e;
        }
    }

    /**
     * Read a video
     *
     * @param hash Hash of the video
     * @return The video, or null if no video found for this hash
     */
    public Video readVideo(String hash) {
        try {
            return videosProxy.readVideo(hash);
        } catch (FeignException e) {
            if (e.status() == 404) return null;
            else throw e;
        }
    }

    /**
     * Update a video
     *
     * @param video the video to update
     * @throws BadRequestException when the video is invalid
     * @throws NotFoundException when no video could be found
     */
    public void updateVideo(Video video) throws BadRequestException, NotFoundException {
        try {
            usersProxy.readUser(video.getAuthor());
        } catch (FeignException e) {
            if (e.status() == 404) throw new NotFoundException();
            else throw e;
        }

        try {
            videosProxy.updateVideo(video.getHash(), video);
        } catch (FeignException e) {
            if (e.status() == 400) throw new BadRequestException();
            else if (e.status() == 404) throw new NotFoundException();
            else throw e;
        }
    }

    /**
     * Delete a video and all reviews linked to that video
     *
     * @param hash Hash of the video
     * @return true if the video could be found, false otherwise
     */
    public boolean deleteVideo(String hash) {
        reviewsProxy.deleteReviewsOfVideo(hash);

        boolean found = true;
        try {
            videosProxy.deleteVideo(hash);
        } catch (FeignException e) {
            if (e.status() == 404) found = false;
            else throw e;
        }
        return found;
    }

    /**
     * Read all videos from a user
     *
     * @param pseudo Pseudo of the user
     * @return List of all videos from this user, or null if the user could not be found
     */
    public Iterable<Video> readVideosFromUser(String pseudo) {
        try {
            usersProxy.readUser(pseudo);
        } catch (FeignException e) {
            if (e.status() == 404) return null;
            else throw e;
        }

        return videosProxy.readVideosFromAuthor(pseudo);
    }

    /**
     * Create a review
     *
     * @param review Review to create
     * @throws BadRequestException when the review is invalid
     * @throws NotFoundException when the user or the video of the review could not be found
     * @throws ConflictException when a review already exists for this user and video
     */
    public void createReview(Review review) throws BadRequestException, NotFoundException, ConflictException {
        try {
            usersProxy.readUser(review.getPseudo());
        } catch (FeignException e) {
            if (e.status() == 404) throw new NotFoundException();
            else throw e;
        }

        try {
            videosProxy.readVideo(review.getHash());
        } catch (FeignException e) {
            if (e.status() == 404) throw new NotFoundException();
            else throw e;
        }

        try {
            reviewsProxy.createReview(review.getPseudo(), review.getHash(), review);
        } catch (FeignException e) {
            if (e.status() == 400) throw new BadRequestException();
            else if (e.status() == 409) throw new ConflictException();
            else throw e;
        }
    }

    /**
     * Read a review
     *
     * @param pseudo Pseudo of the user
     * @param hash Hash of the video
     * @return The review corresponding to this user and video, or null if none could be found
     */
    public Review readReview(String pseudo, String hash) {
        try {
            return reviewsProxy.readReview(pseudo, hash);
        } catch (FeignException e) {
            if (e.status() == 404) return null;
            else throw e;
        }
    }

    /**
     * Update a review
     *
     * @param review Review to update
     * @throws BadRequestException when the review is invalid
     * @throws NotFoundException when the review could not be found
     */
    public void updateReview(Review review) throws BadRequestException, NotFoundException {
        try {
            usersProxy.readUser(review.getPseudo());
        } catch (FeignException e) {
            if (e.status() == 404) throw new NotFoundException();
            else throw e;
        }

        try {
            videosProxy.readVideo(review.getHash());
        } catch (FeignException e) {
            if (e.status() == 404) throw new NotFoundException();
            else throw e;
        }

        try {
            reviewsProxy.updateReview(review.getPseudo(), review.getHash(), review);
        } catch (FeignException e) {
            if (e.status() == 400) throw new BadRequestException();
            else if (e.status() == 404) throw new NotFoundException();
            else throw e;
        }
    }

    /**
     * Delete a review
     *
     * @param pseudo Pseudo of the user
     * @param hash Hash of the video
     * @return true if the review could be found and deleted, false otherwise
     */
    public boolean deleteReview(String pseudo, String hash) {
        boolean found = true;
        try {
            reviewsProxy.deleteReview(pseudo, hash);
        } catch (FeignException e) {
            if (e.status() == 404) found = false;
            else throw e;
        }
        return found;
    }

    /**
     * Read all reviews from a user
     *
     * @param pseudo Pseudo of the user
     * @return The list of all reviews from this user, or null if the user could not be found
     */
    public Iterable<Review> readReviewsFromUser(String pseudo) {
        try {
            usersProxy.readUser(pseudo);
        } catch (FeignException e) {
            if (e.status() == 404) return null;
            else throw e;
        }

        return reviewsProxy.readReviewsFromUser(pseudo);
    }

    /**
     * Read all reviews of a video
     *
     * @param hash Hash of the video
     * @return The list of all reviews of this video, or null if the video could not be found
     */
    public Iterable<Review> readReviewsOfVideo(String hash) {
        try {
            videosProxy.readVideo(hash);
        } catch (FeignException e) {
            if (e.status() == 404) return null;
            else throw e;
        }

        return reviewsProxy.readReviewsOfVideo(hash);
    }

    /**
     * Read the best reviewed videos
     *
     * @return The list of the best reviewed videos
     */
    public Iterable<Video> readBestVideos() {
        return reviewsProxy.readBestVideos();
    }

}
