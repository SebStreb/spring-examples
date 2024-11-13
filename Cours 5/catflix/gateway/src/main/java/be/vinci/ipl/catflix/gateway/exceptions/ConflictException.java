package be.vinci.ipl.catflix.gateway.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ConflictException extends ResponseStatusException {
    public ConflictException() {
        super(HttpStatus.CONFLICT);
    }
}
