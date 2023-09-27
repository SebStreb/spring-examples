package be.vinci.ipl.catflix.videos;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideosRepository extends CrudRepository<Video, String> {
}
