package space.astralbridge.eeg4asd.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GetTaskRequestDTO {
    @NotNull
    String id;

    boolean eegFilePath;

    boolean state;

    boolean result;

    boolean patientID;

    boolean llmResult;
}
