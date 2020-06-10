package fr.pala.accounting.utils.file.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "File is Empty, please select a file")
public final class FileIsEmptyException extends RuntimeException {
    public FileIsEmptyException() {
        super();
    }

    public FileIsEmptyException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public FileIsEmptyException(final String message) {
        super(message);
    }

    public FileIsEmptyException(final Throwable cause) {
        super(cause);
    }
}
