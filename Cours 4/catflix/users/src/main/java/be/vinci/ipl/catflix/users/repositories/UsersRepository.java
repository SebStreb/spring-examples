package be.vinci.ipl.catflix.users.repositories;

import be.vinci.ipl.catflix.users.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends CrudRepository<User, String> {
}
