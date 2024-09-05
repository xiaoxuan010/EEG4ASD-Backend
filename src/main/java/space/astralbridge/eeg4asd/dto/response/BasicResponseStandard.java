package space.astralbridge.eeg4asd.dto.response;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BasicResponseStandard<T> {
    private int code;
    private String msg;
    private T data;

    public BasicResponseStandard(HttpStatus status, T data) {
        this.code = status.value();
        this.msg = status.getReasonPhrase();
        this.data = data;
    }
}
