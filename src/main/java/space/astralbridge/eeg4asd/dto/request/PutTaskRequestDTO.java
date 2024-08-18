package space.astralbridge.eeg4asd.dto.request;

import java.time.Instant;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PutTaskRequestDTO {
    @NotNull
    public String _id;

    public String eegFilePath;
    public String state;
    public Instant updatedAt;
    public String result;
    public String patientID;
}
