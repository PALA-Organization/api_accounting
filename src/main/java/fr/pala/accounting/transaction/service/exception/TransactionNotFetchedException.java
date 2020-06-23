package fr.pala.accounting.transaction.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Transaction Not Fetched")
public final class TransactionNotFetchedException extends RuntimeException {

    public TransactionNotFetchedException() {
        super();
    }

    public TransactionNotFetchedException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public TransactionNotFetchedException(final String message) {
        super(message);
    }

    public TransactionNotFetchedException(final Throwable cause) {
        super(cause);
    }
}