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
import space.astralbridge.eeg4asd.dto.request.UserRegisterPostRequestDTO;
import space.astralbridge.eeg4asd.dto.response.UserLoginPostResponseDTO;
import space.astralbridge.eeg4asd.dto.response.UserRegisterPostResponseDTO;
import space.astralbridge.eeg4asd.dto.response.UserVerifyPostResponseDTO;
import space.astralbridge.eeg4asd.model.User;
import space.astralbridge.eeg4asd.repository.UserRepository;
import space.astralbridge.eeg4asd.service.common.JwtManagementService;
import space.astralbridge.eeg4asd.service.common.KeyManagementService;
import space.astralbridge.eeg4asd.service.common.UserManagementService;
import space.astralbridge.eeg4asd.service.exception.UserLoginUserNotExistException;
import space.astralbridge.eeg4asd.service.exception.UserLoginUserOrPwdErrorException;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserAuthService {
    private final KeyManagementService keyManagementService;

    private final UserRepository userRepository;

    private final JwtManagementService jwtManagementService;

    private final UserManagementService userManagementService;

    public UserRegisterPostResponseDTO registerUser(UserRegisterPostRequestDTO reqDTO)
            throws InvalidKeyException, NoSuchAlgorithmException {
        User newUser = userManagementService.createUser(reqDTO.getUsername(), reqDTO.getPwd(), reqDTO.getRole());

        return new UserRegisterPostResponseDTO(newUser.get_id());
    }

    public UserVerifyPostResponseDTO verifyUsername(String username) {
        return new UserVerifyPostResponseDTO(userManagementService.isUsernameValid(username));
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
