package be.vinci.ipl.catflix.mockvideos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Video {
    private String hash;
    private String name;
    private String author;
    private int creationYear;
    private int duration; // in seconds
    private String url;

    public boolean invalid() {
        return hash == null || hash.isBlank() ||
                name == null || name.isBlank() ||
                author == null || author.isBlank() ||
                creationYear < 1970 || creationYear > LocalDate.now().getYear() ||
                duration <= 0 ||
                url == null || url.isBlank();
    }
}
