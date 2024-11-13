package be.vinci.ipl.catflix.mockauthentication;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Credentials {
    private String pseudo;
    private String password;
}
