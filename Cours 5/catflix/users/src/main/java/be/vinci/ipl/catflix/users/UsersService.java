package be.vinci.ipl.catflix.users;

import be.vinci.ipl.catflix.users.models.User;
import be.vinci.ipl.catflix.users.models.UserWithCredentials;
import be.vinci.ipl.catflix.users.repositories.AuthenticationProxy;
import be.vinci.ipl.catflix.users.repositories.ReviewsProxy;
import be.vinci.ipl.catflix.users.repositories.UsersRepository;
import be.vinci.ipl.catflix.users.repositories.VideosProxy;
import org.springframework.stereotype.Service;

@Service
public class UsersService {

    private final UsersRepository repository;
    private final AuthenticationProxy authenticationProxy;
    private final VideosProxy videosProxy;
    private final ReviewsProxy reviewsProxy;

    public UsersService(UsersRepository repository, AuthenticationProxy authenticationProxy, VideosProxy videosProxy, ReviewsProxy reviewsProxy) {
        this.repository = repository;
        this.authenticationProxy = authenticationProxy;
        this.videosProxy = videosProxy;
        this.reviewsProxy = reviewsProxy;
    }

    /**
     * Creates a new user in repository
     *
     * @param user the information of the user
     * @return true if the user was created, false if another user exists with the same pseudo
     */
    public boolean createOne(UserWithCredentials user) {
        if (repository.existsById(user.getPseudo())) return false;

        // Potential error results (400, 409) should not happen as this service manages consistency
        authenticationProxy.createOne(user.getPseudo(), user.toCredentials());

        repository.save(user.toUser());
        return true;
    }

    /**
     * Reads a user in repository
     *
     * @param pseudo the pseudo of the user
     * @return the user, or null if the user couldn't be found
     */
    public User readOne(String pseudo) {
        return repository.findById(pseudo).orElse(null);
    }

    /**
     * Updates a user in repository
     *
     * @param user New values of the user
     * @return true if the user was updated, or false if the user couldn't be found
     */
    public boolean updateOne(UserWithCredentials user) {
        if (!repository.existsById(user.getPseudo())) return false;

        // Potential error results (400, 404) should not happen as this service manages consistency
        authenticationProxy.updateOne(user.getPseudo(), user.toCredentials());

        repository.save(user.toUser());
        return true;
    }

    /**
     * Deletes a user from repository and all reviews and videos associated with it
     *
     * @param pseudo the pseudo of the user
     * @return true if the user was deleted, or false if the user couldn't be found
     */
    public boolean deleteOne(String pseudo) {
        if (!repository.existsById(pseudo)) return false;

        reviewsProxy.deleteFromUser(pseudo);
        videosProxy.deleteFromUser(pseudo);

        // Potential error results (404) should not happen as this service manages consistency
        authenticationProxy.deleteOne(pseudo);

        repository.deleteById(pseudo);
        return true;
    }

}
