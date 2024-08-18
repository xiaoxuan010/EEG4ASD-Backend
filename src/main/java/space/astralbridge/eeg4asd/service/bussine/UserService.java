package space.astralbridge.eeg4asd.service.bussine;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import space.astralbridge.eeg4asd.dto.common.UserBasicInfo;
import space.astralbridge.eeg4asd.dto.request.GetUserRequestDTO;
import space.astralbridge.eeg4asd.dto.request.PostUserParentRequestDTO;
import space.astralbridge.eeg4asd.dto.request.PostUserRoleRequestDTO;
import space.astralbridge.eeg4asd.dto.response.GetUserResponseDTO;
import space.astralbridge.eeg4asd.model.User;
import space.astralbridge.eeg4asd.repository.UserRepository;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserService {
    private final UserRepository userRepository;

    public GetUserResponseDTO getUserBasicInfo(GetUserRequestDTO requestDTO) {
        User user = userRepository.findBy_id(requestDTO.getId());
        return new GetUserResponseDTO(user.getUsername(), user.getRole(), null);
    }

    public GetUserResponseDTO getUserInfo(GetUserRequestDTO requestDTO) {
        User user = userRepository.findBy_id(requestDTO.getId());
        return new GetUserResponseDTO(user.getUsername(), user.getRole(), user.getParentId());
    }

    public void setUserRole(PostUserRoleRequestDTO requestDTO, HttpServletRequest request) {
        User user = userRepository
                .findBy_id(Optional.ofNullable(request.getAttribute("uid")).orElseThrow().toString());
        if (user.getRole() == null) {
            user.setRole(requestDTO.getRole());
            userRepository.save(user);
        } else {
            throw new IllegalArgumentException("User role already set");
        }
        return;
    }

    public void setParent(PostUserParentRequestDTO requestDTO, HttpServletRequest request) {
        User user = userRepository
                .findBy_id(Optional.ofNullable(request.getAttribute("uid")).orElseThrow().toString());
        if (user.getParentId() != null) {
            throw new IllegalArgumentException("User parent already set");
        } else if (user.getRole() == null) {
            throw new IllegalArgumentException("User role not set");
        } else if (!"c".equals(user.getRole())) {
            throw new IllegalArgumentException("Only customers can have parents");
        } else {
            User parent = userRepository.findBy_id(requestDTO.getParentId());
            if (parent == null) {
                throw new IllegalArgumentException("Parent not found");
            } else if (!"b".equals(parent.getRole())) {
                throw new IllegalArgumentException("Parent is not a business");
            } else {
                user.setParentId(requestDTO.getParentId());
                userRepository.save(user);
            }
        }
        return;
    }

    public List<UserBasicInfo> getChildrenList(HttpServletRequest request) {
        User user = userRepository
                .findBy_id(Optional.ofNullable(request.getAttribute("uid")).orElseThrow().toString());
        if (user.getRole() == null) {
            throw new IllegalArgumentException("User role not set");
        } else if (!"b".equals(user.getRole())) {
            throw new IllegalArgumentException("Only businesses can have children");
        } else {
            return userRepository.findByParentId(user.get_id())
                    .stream()
                    .map(UserBasicInfo::new)
                    .collect(Collectors.toList());

        }
    }

}
