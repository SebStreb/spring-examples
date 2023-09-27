package be.vinci.ipl.catflix.users;

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
@Entity(name = "users")
public class User {
    @Id
    private String pseudo;
    private String firstname;
    private String lastname;
}
