package fr.pala.accounting.account.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Account Does Not Exist")
public final class AccountDoesNotExistException extends RuntimeException {

    public AccountDoesNotExistException() {
        super();
    }

    public AccountDoesNotExistException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public AccountDoesNotExistException(final String message) {
        super(message);
    }

    public AccountDoesNotExistException(final Throwable cause) {
        super(cause);
    }
}