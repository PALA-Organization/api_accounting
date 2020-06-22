package fr.pala.accounting.account.infrastructure.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Account Not Fetched")
public final class AccountNotFetchedException extends RuntimeException {

    public AccountNotFetchedException() {
        super();
    }

    public AccountNotFetchedException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public AccountNotFetchedException(final String message) {
        super(message);
    }

    public AccountNotFetchedException(final Throwable cause) {
        super(cause);
    }
}