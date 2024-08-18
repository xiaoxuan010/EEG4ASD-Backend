package space.astralbridge.eeg4asd.interceptor.exception;

public class AuthInvalidTokenException extends IllegalArgumentException {
    public AuthInvalidTokenException() {
        super("Invalid token.");
    }
}
