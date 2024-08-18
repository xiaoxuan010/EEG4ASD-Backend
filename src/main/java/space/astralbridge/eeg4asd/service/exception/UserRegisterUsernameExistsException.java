package space.astralbridge.eeg4asd.service.exception;

public class UserRegisterUsernameExistsException extends IllegalArgumentException {
    public UserRegisterUsernameExistsException(String username) {
        super("Username " + username + " already exists.");
    }
}
