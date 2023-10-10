package be.vinci.ipl.catflix.videos;

import jakarta.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideosRepository extends CrudRepository<Video, String> {

    Iterable<Video> findByAuthor(String author);

    @Transactional
    void deleteByAuthor(String author);

}
