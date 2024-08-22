package space.astralbridge.eeg4asd.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import space.astralbridge.eeg4asd.dto.common.UserBasicInfo;
import space.astralbridge.eeg4asd.dto.request.GetUserRequestDTO;
import space.astralbridge.eeg4asd.dto.request.PostUserParentRequestDTO;
import space.astralbridge.eeg4asd.dto.request.PostUserRoleRequestDTO;
import space.astralbridge.eeg4asd.dto.response.GetUserResponseDTO;
import space.astralbridge.eeg4asd.model.User;
import space.astralbridge.eeg4asd.service.bussine.UserService;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping("/{uid}")
    public GetUserResponseDTO handleUserGet(@PathVariable("uid") String uid, HttpServletRequest request) {
        GetUserRequestDTO requestDTO = new GetUserRequestDTO(uid);
        return userService.getUserBasicInfo(requestDTO, (User) request.getAttribute("authUser"));
    }

    @GetMapping
    public GetUserResponseDTO handleUserDefaultGet(HttpServletRequest request) {
        return userService.getDefaultUserInfo((User) request.getAttribute("authUser"));
    }

    @PostMapping("/role")
    public void handleUserRolePost(@Valid @RequestBody PostUserRoleRequestDTO requestDTO,
            HttpServletRequest request) {

        userService.setUserRole(requestDTO, (User) request.getAttribute("authUser"));
        return;
    }

    @PostMapping("/parent")
    public void handleUserParentPost(@Valid @RequestBody PostUserParentRequestDTO requestDTO,
            HttpServletRequest request) {
        userService.setParent(requestDTO, (User) request.getAttribute("authUser"));
        return;
    }

    @GetMapping("/children")
    public List<UserBasicInfo> handleUserChildGet(HttpServletRequest request) {
        return userService.getChildrenList((User) request.getAttribute("authUser"));
    }
}
