package fr.pala.accounting.transaction.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Transaction Not Found")
public final class TransactionNotFoundException extends RuntimeException {

    public TransactionNotFoundException() {
        super();
    }

    public TransactionNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public TransactionNotFoundException(final String message) {
        super(message);
    }

    public TransactionNotFoundException(final Throwable cause) {
        super(cause);
    }
}