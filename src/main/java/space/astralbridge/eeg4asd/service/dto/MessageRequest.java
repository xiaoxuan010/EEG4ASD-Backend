package space.astralbridge.eeg4asd.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MessageRequest {
    private String id;
    private String path;
}
