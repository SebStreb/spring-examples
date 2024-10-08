package be.vinci.ipl.catflix.reviews.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class User {
    private String pseudo;
    private String firstname;
    private String lastname;
}
