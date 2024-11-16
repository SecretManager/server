package application.common.exception;

public class InternalServerErrorException extends ApplicationException {

    public InternalServerErrorException(String message) {
        super(message, 500);
    }
}
