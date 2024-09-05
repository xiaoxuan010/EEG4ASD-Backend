package space.astralbridge.eeg4asd.dto.request;

import jakarta.annotation.Nonnull;
import lombok.Data;

@Data
public class UserChangePasswordPostRequestDTO {
    @Nonnull
    private String oldPwd;

    @Nonnull
    private String newPwd;
}
