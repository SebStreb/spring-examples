package be.vinci.ipl.catflix.videos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class Video {
    private String hash;
    private String name;
    private String author;
    private int creationYear;
    private int duration; // in seconds
    private String url;
}
