package be.vinci.ipl.catflix.mockreviews.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Review {
    private String pseudo;
    private String hash;
    private int rating; // between 0 and 10
    private String comment;
}
