package space.astralbridge.eeg4asd.dto.response;

import lombok.Data;

@Data
public class GetTaskResponseDTO {
    private String _id;

    private String patientID;

    private String eegFilePath;

    private String state;

    private String result;

    private String llmResult;
}
