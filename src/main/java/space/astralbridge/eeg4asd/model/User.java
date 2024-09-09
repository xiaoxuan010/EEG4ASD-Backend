package space.astralbridge.eeg4asd.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import lombok.Data;

@Document(collection = "users")
@Data
public class User {
    @Id
    private String _id = new ObjectId().toHexString();

    private String username;

    private byte[] pwdSecretKey;

    private byte[] password;

    private String role;

    @Field(targetType = FieldType.OBJECT_ID)
    private String parentId;

    String legalName;

    String phoneNumber;

}
