package fr.pala.accounting.account.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Account Not Created")
public final class AccountNotCreatedException extends RuntimeException {

    public AccountNotCreatedException() {
        super();
    }

    public AccountNotCreatedException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public AccountNotCreatedException(final String message) {
        super(message);
    }

    public AccountNotCreatedException(final Throwable cause) {
        super(cause);
    }
}