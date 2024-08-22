package space.astralbridge.eeg4asd.dto.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PostUserParentRequestDTO {
    @Nullable
    String id;

    @NotNull
    String parentId;
}
