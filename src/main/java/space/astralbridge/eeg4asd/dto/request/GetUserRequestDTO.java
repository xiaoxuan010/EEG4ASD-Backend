package space.astralbridge.eeg4asd.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GetUserRequestDTO {
    @NotNull
    private final String id;
}
