package fr.pala.accounting.transaction.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Transaction Not Created")
public final class TransactionNotCreatedException extends RuntimeException {

    public TransactionNotCreatedException() {
        super();
    }

    public TransactionNotCreatedException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public TransactionNotCreatedException(final String message) {
        super(message);
    }

    public TransactionNotCreatedException(final Throwable cause) {
        super(cause);
    }
}