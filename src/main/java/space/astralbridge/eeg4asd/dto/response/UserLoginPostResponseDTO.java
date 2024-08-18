package space.astralbridge.eeg4asd.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserLoginPostResponseDTO {

    @NotNull
    private final String token;
}
