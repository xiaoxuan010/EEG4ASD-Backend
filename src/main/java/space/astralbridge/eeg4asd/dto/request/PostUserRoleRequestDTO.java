package space.astralbridge.eeg4asd.dto.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class PostUserRoleRequestDTO {
    @Nullable
    public String id;

    @NotEmpty
    @Pattern(regexp = "^[bc]$", message = "role must be 'b' or 'c'")
    public String role;
}
