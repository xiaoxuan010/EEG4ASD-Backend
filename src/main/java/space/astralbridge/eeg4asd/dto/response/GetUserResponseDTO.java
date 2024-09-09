package space.astralbridge.eeg4asd.dto.response;

import lombok.Data;

@Data
public class GetUserResponseDTO {
    private final String username;
    private final String role;
    private final String parentId;
    private final String legalName;
    private final String phoneNumber;
}
