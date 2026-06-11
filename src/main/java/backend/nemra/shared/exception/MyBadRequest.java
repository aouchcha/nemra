package backend.nemra.shared.exception;

public class MyBadRequest extends RuntimeException {
    public MyBadRequest(String message) {
        super(message);
    }
}
