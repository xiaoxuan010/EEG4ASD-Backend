package space.astralbridge.eeg4asd.service.exception;

public class UserLoginUserOrPwdErrorException extends IllegalArgumentException {
    public UserLoginUserOrPwdErrorException() {
        super("Username or password error.");
    }

}
