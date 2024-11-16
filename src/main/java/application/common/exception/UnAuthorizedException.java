package application.common.exception;

public class UnAuthorizedException extends ApplicationException {

    public UnAuthorizedException(String message) {
        super(message, 401);
    }
}
