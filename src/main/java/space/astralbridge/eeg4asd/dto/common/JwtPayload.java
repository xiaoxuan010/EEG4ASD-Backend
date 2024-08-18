package space.astralbridge.eeg4asd.dto.common;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class JwtPayload {

    private String uid;

    private Long exp;

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("uid", uid);
        map.put("exp", exp);
        return map;
    }

}
