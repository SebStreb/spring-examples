package be.vinci.ipl.catflix.reviews.repositories;

import be.vinci.ipl.catflix.reviews.models.Review;
import jakarta.persistence.Tuple;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewsRepository extends CrudRepository<Review, Long> {

    boolean existsByPseudoAndHash(String pseudo, String hash);

    Optional<Review> findByPseudoAndHash(String pseudo, String hash);

    @Transactional
    void deleteByPseudoAndHash(String pseudo, String hash);

    Iterable<Review> findByPseudo(String pseudo);

    @Transactional
    void deleteByPseudo(String pseudo);

    Iterable<Review> findByHash(String hash);

    @Transactional
    void deleteByHash(String hash);

    @Query("select hash as videoHash, avg(rating) as avgRating from reviews group by videoHash order by avgRating desc")
    Iterable<Tuple> findBest();

}
