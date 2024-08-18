package space.astralbridge.eeg4asd.interceptor.exception;

public class AuthMissingTokenException extends IllegalArgumentException {
    public AuthMissingTokenException() {
        super("Missing token.");
    }
}
