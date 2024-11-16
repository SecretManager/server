package application.common.exception;

public class BadRequestException extends ApplicationException {

    public BadRequestException(String message) {
        super(message, 400);
    }
}
