package space.astralbridge.eeg4asd.dto.request.llm;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PostChatRequestDTO {
    @Nullable
    private String taskId;

    @NotBlank
    private String message;
}
