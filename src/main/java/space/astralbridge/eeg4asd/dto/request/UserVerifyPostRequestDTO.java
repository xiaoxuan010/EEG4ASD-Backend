package space.astralbridge.eeg4asd.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserVerifyPostRequestDTO {
    @NotNull
    private String username;
}
