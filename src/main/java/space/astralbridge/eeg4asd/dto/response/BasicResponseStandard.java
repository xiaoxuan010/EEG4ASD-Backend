package space.astralbridge.eeg4asd.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BasicResponseStandard<T> {
    private int code;
    private String msg;
    private T data;
}
