package space.astralbridge.eeg4asd.model;

import java.time.Instant;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import lombok.Data;

@Data
@Document(collection = "tasks")
public class Task {

    @Id
    private String _id = new ObjectId().toHexString();

    private String eegFilePath;
    private String state;
    private Instant updatedAt;
    private String result;

    @Field(targetType = FieldType.OBJECT_ID)
    private String patientID;

    private String llmResult;

}
