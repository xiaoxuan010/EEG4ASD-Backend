package space.astralbridge.eeg4asd.dto.request;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateTaskRequestDTO {
    @NotNull(message = "eegFile is required")
    private MultipartFile eegFile;

    @NotBlank(message = "patientID is required")
    private String patientID;

}
