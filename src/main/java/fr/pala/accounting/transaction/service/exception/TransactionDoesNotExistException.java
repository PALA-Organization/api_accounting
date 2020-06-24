package fr.pala.accounting.transaction.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Transaction Does not Exist")
public final class TransactionDoesNotExistException extends RuntimeException {

    public TransactionDoesNotExistException() {
        super();
    }

    public TransactionDoesNotExistException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public TransactionDoesNotExistException(final String message) {
        super(message);
    }

    public TransactionDoesNotExistException(final Throwable cause) {
        super(cause);
    }
}