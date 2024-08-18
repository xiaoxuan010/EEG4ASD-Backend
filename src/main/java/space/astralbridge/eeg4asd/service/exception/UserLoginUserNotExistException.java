package space.astralbridge.eeg4asd.service.exception;

public class UserLoginUserNotExistException extends IllegalArgumentException {
    public UserLoginUserNotExistException(String username) {
        super("Username " + username + " does not exist.");
    }

}
