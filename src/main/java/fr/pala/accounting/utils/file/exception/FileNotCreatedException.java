package fr.pala.accounting.utils.file.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR, reason = "An Internal Error occured during the download of your file")
public final class FileNotCreatedException extends RuntimeException {
    public FileNotCreatedException() {
        super();
    }

    public FileNotCreatedException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public FileNotCreatedException(final String message) {
        super(message);
    }

    public FileNotCreatedException(final Throwable cause) {
        super(cause);
    }
}
