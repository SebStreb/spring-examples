package be.vinci.ipl.catflix.videos;

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
@Entity
public class Video {
    @Id
    private String hash;
    private String name;
    private String author;
    @Column(name = "creation_year")
    private int creationYear;
    private int duration; // in seconds
    private String url;
}
