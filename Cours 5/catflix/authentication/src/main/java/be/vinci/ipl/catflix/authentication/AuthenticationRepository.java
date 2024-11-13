package be.vinci.ipl.catflix.authentication;

import be.vinci.ipl.catflix.authentication.models.SafeCredentials;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthenticationRepository extends CrudRepository<SafeCredentials, String> {
}
