package space.astralbridge.eeg4asd.interceptor;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.auth0.jwt.exceptions.JWTVerificationException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import space.astralbridge.eeg4asd.interceptor.exception.AuthInvalidTokenException;
import space.astralbridge.eeg4asd.interceptor.exception.AuthMissingTokenException;
import space.astralbridge.eeg4asd.model.User;
import space.astralbridge.eeg4asd.repository.UserRepository;
import space.astralbridge.eeg4asd.service.common.JwtManagementService;

@Component
@Data
@Slf4j
public class AuthInterceptor implements HandlerInterceptor {
    private final JwtManagementService jwtManagementService;
    private final UserRepository userRepository;

    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull Object handler)
            throws Exception {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader == null ? null : authHeader.replace("Bearer ", "");

        if (token == null) {
            throw new AuthMissingTokenException();
        } else {
            try {
                String uid = jwtManagementService.verifyToken(token);

                User authUser = userRepository.findBy_id(uid);
                request.setAttribute("authUser", authUser);

            } catch (JWTVerificationException e) {
                log.warn(e.getMessage());
                e.printStackTrace();
                throw new AuthInvalidTokenException();
            }
            return true;
        }

    }
}
