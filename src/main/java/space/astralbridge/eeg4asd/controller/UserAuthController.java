package space.astralbridge.eeg4asd.controller;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import space.astralbridge.eeg4asd.dto.request.UserLoginPostRequestDTO;
import space.astralbridge.eeg4asd.dto.request.UserRegisterPostRequestDTO;
import space.astralbridge.eeg4asd.dto.request.UserVerifyPostRequestDTO;
import space.astralbridge.eeg4asd.dto.response.UserLoginPostResponseDTO;
import space.astralbridge.eeg4asd.dto.response.UserRegisterPostResponseDTO;
import space.astralbridge.eeg4asd.dto.response.UserVerifyPostResponseDTO;
import space.astralbridge.eeg4asd.service.auth.UserAuthService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserAuthController {
    private final UserAuthService userService;

    @PostMapping("/verify")
    public UserVerifyPostResponseDTO handleUserVerifyPost(@RequestBody @Valid UserVerifyPostRequestDTO requestDTO) {
        return userService.verifyUsername(requestDTO.getUsername());
    }

    @PostMapping("/signup")
    public UserRegisterPostResponseDTO handleUserRegisterPost(
            @RequestBody @Valid UserRegisterPostRequestDTO requestDTO)
            throws InvalidKeyException, NoSuchAlgorithmException {
        return userService.registerUser(requestDTO);
    }

    @PostMapping("/login")
    public UserLoginPostResponseDTO handleUserLoginPost(@RequestBody @Valid UserLoginPostRequestDTO requestDTO)
            throws InvalidKeyException, NoSuchAlgorithmException {
        return userService.Login(requestDTO);
    }

}
