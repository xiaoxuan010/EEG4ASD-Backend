package space.astralbridge.eeg4asd.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "jwt")
@Validated
@Data
public class JwtConfig {
    @NotBlank(message = "JWT secret cannot be blank")
    private String secret;

    @NotNull(message = "JWT expiration cannot be null")
    private Integer expiration = 86400;

}
