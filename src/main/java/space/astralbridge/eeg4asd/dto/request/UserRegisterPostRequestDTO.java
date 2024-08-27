package space.astralbridge.eeg4asd.dto.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserRegisterPostRequestDTO {
    @NotNull
    private String username;

    @NotNull
    private String pwd;

    @Nullable
    private String role;
}
