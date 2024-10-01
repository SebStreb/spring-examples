package be.vinci.ipl.catflix.videos.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity(name = "videos")
public class Video {
    @Id @Column(nullable = false)
    private String hash;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String author;

    @Column(name = "creation_year", nullable = false)
    private int creationYear;

    @Column(nullable = false)
    private int duration; // in seconds

    private String url;

    public boolean invalid() {
        return hash == null || hash.isBlank() ||
                name == null || name.isBlank() ||
                author == null || author.isBlank() ||
                creationYear < 1970 ||
                duration <= 0;
    }
}
