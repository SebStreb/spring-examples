package be.vinci.ipl.catflix.reviews.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    @JsonIgnore
    private long id;

    @Column(nullable = false)
    private String pseudo;

    @Column(nullable = false)
    private String hash;

    @Column(nullable = false)
    private int rating; // between 0 and 10

    private String comment;

    public boolean invalid() {
        return pseudo == null || pseudo.isBlank() ||
                hash == null || hash.isBlank() ||
                rating < 0 || rating > 10;
    }
}
