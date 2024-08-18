package space.astralbridge.eeg4asd.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import jakarta.annotation.Nonnull;
import space.astralbridge.eeg4asd.model.Task;

public interface TaskRepository extends MongoRepository<Task, String> {

    public Optional<Task> findBy_id(@Nonnull String _id);

    public List<Task> findByPatientID(@Nonnull String patientID);

    public List<Task> findByPatientIDIn(@Nonnull List<String> patientIDs);

}
