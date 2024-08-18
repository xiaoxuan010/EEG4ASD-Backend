package space.astralbridge.eeg4asd.service.mq;

import lombok.Data;

@Data
public class MessageResponse {
    private String id;
    private String result;
}
