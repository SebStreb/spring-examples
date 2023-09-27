package be.vinci.ipl.catflix.videos;

import jakarta.persistence.*;
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
    @Id
    private String hash;
    private String name;
    private String author;
    @Column(name = "creation_year")
    private int creationYear;
    private int duration; // in seconds
    private String url;
}
