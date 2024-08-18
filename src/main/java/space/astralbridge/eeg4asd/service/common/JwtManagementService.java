package space.astralbridge.eeg4asd.service.common;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import space.astralbridge.eeg4asd.config.JwtConfig;
import space.astralbridge.eeg4asd.dto.common.JwtPayload;

@Service
@Slf4j
@Data
public class JwtManagementService {
    private final JwtConfig jwtConfig;

    public String generateToken(JwtPayload payload) {
        log.debug("为用户 {} 生成 JWT", payload.getUid());

        if (payload.getExp() == null) {
            Date now = new Date();
            Long expirationTime = now.getTime() + jwtConfig.getExpiration() * 1000L;
            payload.setExp(expirationTime);
        }

        String token = JWT.create().withPayload(payload.toMap()).sign(Algorithm.HMAC256(jwtConfig.getSecret()));

        return token;
    }

    public String verifyToken(String token) {

        return JWT.require(Algorithm.HMAC256(jwtConfig.getSecret())).build().verify(token).getClaim("uid").asString();
    }
}
