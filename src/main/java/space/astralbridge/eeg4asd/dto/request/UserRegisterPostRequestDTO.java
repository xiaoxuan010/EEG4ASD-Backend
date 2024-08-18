package space.astralbridge.eeg4asd.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserRegisterPostRequestDTO {
    @NotNull
    private String username;

    @NotNull
    private String pwd;
}
