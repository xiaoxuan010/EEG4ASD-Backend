package space.astralbridge.eeg4asd.dto.common;

import lombok.Data;
import space.astralbridge.eeg4asd.model.User;

@Data
public class UserBasicInfo {
    String _id;
    String username;
    String role;
    String legalName;
    String phoneNumber;

    public UserBasicInfo(User user) {
        this._id = user.get_id();
        this.username = user.getUsername();
        this.role = user.getRole();
        this.legalName = user.getLegalName();
        this.phoneNumber = user.getPhoneNumber();
    }
}
