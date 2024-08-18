package space.astralbridge.eeg4asd.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVerifyPostResponseDTO {
    @JsonProperty("isUsernameValid")
    private boolean isUsernameValid;
}
