package be.vinci.ipl.catflix.gateway.models;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class User {
    private String pseudo;
    private String firstname;
    private String lastname;
}
