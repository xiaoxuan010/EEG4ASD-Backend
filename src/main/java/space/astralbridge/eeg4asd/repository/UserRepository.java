package space.astralbridge.eeg4asd.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.lang.NonNull;

import space.astralbridge.eeg4asd.model.User;

public interface UserRepository extends MongoRepository<User, String> {
    public User findByUsername(String username);

    public User findBy_id(String _id);

    public boolean existsByUsername(String username);

    @NonNull
    public List<User> findAll();

    @NonNull
    public List<User> findByParentId(String parentId);
}
