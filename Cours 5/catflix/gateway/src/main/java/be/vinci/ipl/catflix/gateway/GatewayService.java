package be.vinci.ipl.catflix.gateway;

import be.vinci.ipl.catflix.gateway.data.AuthenticationProxy;
import be.vinci.ipl.catflix.gateway.data.ReviewsProxy;
import be.vinci.ipl.catflix.gateway.data.UsersProxy;
import be.vinci.ipl.catflix.gateway.data.VideosProxy;
import be.vinci.ipl.catflix.gateway.exceptions.*;
import be.vinci.ipl.catflix.gateway.models.*;
import feign.FeignException;
import org.springframework.stereotype.Service;

@Service
public class GatewayService {

    private final AuthenticationProxy authenticationProxy;
    private final ReviewsProxy reviewsProxy;
    private final UsersProxy usersProxy;
    private final VideosProxy videosProxy;

    public GatewayService(AuthenticationProxy authenticationProxy, ReviewsProxy reviewsProxy, UsersProxy usersProxy, VideosProxy videosProxy) {
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
     * @throws BadRequestException   when the credentials are invalid
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
     * @throws UnauthorizedException when the credentials are incorrect
     * @throws ForbiddenException    when the user pseudo is not the expected one
     */
    public void verify(String token,
                       String expectedPseudo) throws UnauthorizedException, ForbiddenException {
        try {
            String userPseudo = authenticationProxy.verify(token);
            if (!userPseudo.equals(expectedPseudo)) throw new ForbiddenException();
        } catch (FeignException e) {
            if (e.status() == 401) throw new UnauthorizedException();
            else throw e;
        }
    }

    /**
     * Create user
     *
     * @param user User to create with credentials
     * @throws BadRequestException When the user is not valid
     * @throws ConflictException   When the user already exists
     */
    public void createUser(UserWithCredentials user) throws BadRequestException, ConflictException {
        try {
            usersProxy.createUser(user.getPseudo(), user);
        } catch (FeignException e) {
            if (e.status() == 400) throw new BadRequestException();
            else if (e.status() == 409) throw new ConflictException();
            else throw e;
        }
    }

    /**
     * Read user information
     *
     * @param pseudo Pseudo of the user
     * @return User information
     * @throws NotFoundException when the user could not be found
     */
    public User readUser(String pseudo) throws NotFoundException {
        try {
            return usersProxy.readUser(pseudo);
        } catch (FeignException e) {
            if (e.status() == 404) throw new NotFoundException();
            else throw e;
        }
    }

    /**
     * Update user
     *
     * @param user User to create with credentials
     * @throws BadRequestException When the user is not valid
     * @throws NotFoundException   When the user couldn't be found
     */
    public void updateUser(UserWithCredentials user) throws BadRequestException, NotFoundException {
        try {
            usersProxy.updateUser(user.getPseudo(), user);
        } catch (FeignException e) {
            if (e.status() == 400) throw new BadRequestException();
            else if (e.status() == 404) throw new NotFoundException();
            else throw e;
        }
    }

    /**
     * Delete user
     *
     * @param pseudo Pseudo of the user
     * @throws NotFoundException when the user could not be found
     */
    public void deleteUser(String pseudo) throws NotFoundException {
        try {
            usersProxy.deleteUser(pseudo);
        } catch (FeignException e) {
            if (e.status() == 404) throw new NotFoundException();
            else throw e;
        }
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
     * @throws ConflictException   when a video already exists for this hash
     */
    public void createVideo(Video video) throws BadRequestException, ConflictException {
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
     * @return The video
     * @throws NotFoundException when the video couldn't be found
     */
    public Video readVideo(String hash) throws NotFoundException {
        try {
            return videosProxy.readVideo(hash);
        } catch (FeignException e) {
            if (e.status() == 404) throw new NotFoundException();
            else throw e;
        }
    }

    /**
     * Update a video
     *
     * @param video the video to update
     * @throws BadRequestException when the video is invalid
     * @throws NotFoundException   when no video could be found
     */
    public void updateVideo(Video video) throws BadRequestException, NotFoundException {
        try {
            videosProxy.updateVideo(video.getHash(), video);
        } catch (FeignException e) {
            if (e.status() == 400) throw new BadRequestException();
            else if (e.status() == 404) throw new NotFoundException();
            else throw e;
        }
    }

    /**
     * Delete a video
     *
     * @param hash Hash of the video
     * @throws NotFoundException when the video could not be found
     */
    public void deleteVideo(String hash) throws NotFoundException {
        try {
            videosProxy.deleteVideo(hash);
        } catch (FeignException e) {
            if (e.status() == 404) throw new NotFoundException();
            else throw e;
        }
    }

    /**
     * Read all videos from a user
     *
     * @param pseudo Pseudo of the user
     * @return List of all videos from this user, or null if the user could not be found
     */
    public Iterable<Video> readVideosFromUser(String pseudo) {
        return videosProxy.readVideosFromAuthor(pseudo);
    }

    /**
     * Create a review
     *
     * @param review Review to create
     * @throws BadRequestException when the review is invalid
     * @throws ConflictException   when a review already exists for this user and video
     */
    public void createReview(Review review) throws BadRequestException, ConflictException {
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
     * @param hash   Hash of the video
     * @return The review corresponding to this user and video
     * @throws NotFoundException when the review could not be found
     */
    public Review readReview(String pseudo, String hash) throws NotFoundException {
        try {
            return reviewsProxy.readReview(pseudo, hash);
        } catch (FeignException e) {
            if (e.status() == 404) throw new NotFoundException();
            else throw e;
        }
    }

    /**
     * Update a review
     *
     * @param review Review to update
     * @throws BadRequestException when the review is invalid
     * @throws NotFoundException   when the review could not be found
     */
    public void updateReview(Review review) throws BadRequestException, NotFoundException {
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
     * @param hash   Hash of the video
     * @throws NotFoundException when the review could not be found
     */
    public void deleteReview(String pseudo, String hash) throws NotFoundException {
        try {
            reviewsProxy.deleteReview(pseudo, hash);
        } catch (FeignException e) {
            if (e.status() == 404) throw new NotFoundException();
            else throw e;
        }
    }

    /**
     * Read all reviews from a user
     *
     * @param pseudo Pseudo of the user
     * @return The list of all reviews from this user
     */
    public Iterable<Review> readReviewsFromUser(String pseudo) {
        return reviewsProxy.readReviewsFromUser(pseudo);
    }

    /**
     * Read all reviews of a video
     *
     * @param hash Hash of the video
     * @return The list of all reviews of this video
     */
    public Iterable<Review> readReviewsOfVideo(String hash) {
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
