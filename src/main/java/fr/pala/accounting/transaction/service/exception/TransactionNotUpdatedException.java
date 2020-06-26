package fr.pala.accounting.transaction.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Transaction Not Updated")
public final class TransactionNotUpdatedException extends RuntimeException {

    public TransactionNotUpdatedException() {
        super();
    }

    public TransactionNotUpdatedException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public TransactionNotUpdatedException(final String message) {
        super(message);
    }

    public TransactionNotUpdatedException(final Throwable cause) {
        super(cause);
    }
}