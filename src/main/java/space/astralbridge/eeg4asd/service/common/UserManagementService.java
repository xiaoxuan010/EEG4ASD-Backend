package space.astralbridge.eeg4asd.service.common;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import space.astralbridge.eeg4asd.model.User;
import space.astralbridge.eeg4asd.repository.UserRepository;
import space.astralbridge.eeg4asd.service.exception.UserRegisterUsernameExistsException;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserManagementService {
    private final UserRepository userRepository;

    private final KeyManagementService keyManagementService;

    public boolean isUsernameValid(String username) {
        return userRepository.findByUsername(username) == null;
    }

    public User createUser(String username, String password) throws NoSuchAlgorithmException, InvalidKeyException {
        if (!isUsernameValid(username)) {
            throw new UserRegisterUsernameExistsException(username);
        }

        SecretKey secretKey = keyManagementService.generateHmacKey();
        byte[] pwdSecretKey = secretKey.getEncoded();
        byte[] pwdHash = keyManagementService.HmacSHA256(password, secretKey);

        User user = new User();
        user.setUsername(username);
        user.setPwdSecretKey(pwdSecretKey);
        user.setPassword(pwdHash);
        user.setRole("undefined");

        userRepository.save(user);
        return user;
    }

    

}
