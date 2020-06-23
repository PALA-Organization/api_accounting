package fr.pala.accounting.account.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Account Not Updated")
public final class AccountNotUpdatedException extends RuntimeException {

    public AccountNotUpdatedException() {
        super();
    }

    public AccountNotUpdatedException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public AccountNotUpdatedException(final String message) {
        super(message);
    }

    public AccountNotUpdatedException(final Throwable cause) {
        super(cause);
    }
}