package space.astralbridge.eeg4asd.service.auth;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import space.astralbridge.eeg4asd.dto.common.JwtPayload;
import space.astralbridge.eeg4asd.dto.request.UserLoginPostRequestDTO;
import space.astralbridge.eeg4asd.dto.response.UserLoginPostResponseDTO;
import space.astralbridge.eeg4asd.dto.response.UserVerifyPostResponseDTO;
import space.astralbridge.eeg4asd.model.User;
import space.astralbridge.eeg4asd.repository.UserRepository;
import space.astralbridge.eeg4asd.service.common.JwtManagementService;
import space.astralbridge.eeg4asd.service.common.KeyManagementService;
import space.astralbridge.eeg4asd.service.exception.UserLoginUserNotExistException;
import space.astralbridge.eeg4asd.service.exception.UserLoginUserOrPwdErrorException;
import space.astralbridge.eeg4asd.service.exception.UserRegisterUsernameExistsException;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserAuthService {
    private final KeyManagementService keyManagementService;

    private final UserRepository userRepository;

    private final JwtManagementService jwtManagementService;

    public User createUser(String username, String password)
            throws InvalidKeyException, NoSuchAlgorithmException {

        log.info("用户 {} 注册", username);

        if (!isUsernameValid(username)) {
            throw new UserRegisterUsernameExistsException(username);
        }

        User user = new User();
        user.setUsername(username);

        SecretKey secretKey = keyManagementService.generateHmacKey();
        user.setPwdSecretKey(secretKey.getEncoded());
        user.setPassword(keyManagementService.HmacSHA256(password, secretKey));

        userRepository.save(user);

        return user;
    }

    public UserVerifyPostResponseDTO verifyUsername(String username) {
        return new UserVerifyPostResponseDTO(isUsernameValid(username));
    }

    public UserLoginPostResponseDTO Login(UserLoginPostRequestDTO requestDTO)
            throws InvalidKeyException, NoSuchAlgorithmException {

        String uid = userPwdAuth(requestDTO.getUsername(), requestDTO.getPwd());
        if (uid == null) {
            throw new UserLoginUserOrPwdErrorException();
        } else {
            JwtPayload jwtPayload = new JwtPayload();
            jwtPayload.setUid(uid);
            return new UserLoginPostResponseDTO(jwtManagementService.generateToken(jwtPayload));
        }

    }

    private boolean isUsernameValid(String username) {
        return userRepository.findByUsername(username) == null;
    }

    private String userPwdAuth(String inputUserName, String inputPwd)
            throws InvalidKeyException, NoSuchAlgorithmException {
        User user = userRepository.findByUsername(inputUserName);
        if (user == null) {
            throw new UserLoginUserNotExistException(inputUserName);
        }

        SecretKey secretKey = new SecretKeySpec(user.getPwdSecretKey(), "HmacSHA256");

        byte[] pwdHash = keyManagementService.HmacSHA256(inputPwd, secretKey);

        if (MessageDigest.isEqual(pwdHash, user.getPassword())) {
            return user.get_id();
        } else {
            return null;
        }

    }

}
