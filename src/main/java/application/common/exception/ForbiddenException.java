package application.common.exception;

public class ForbiddenException extends ApplicationException {

    public ForbiddenException(String message) {
        super(message, 403);
    }
}
