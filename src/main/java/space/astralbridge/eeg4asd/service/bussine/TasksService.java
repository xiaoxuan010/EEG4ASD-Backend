package space.astralbridge.eeg4asd.service.bussine;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import space.astralbridge.eeg4asd.model.Task;
import space.astralbridge.eeg4asd.model.User;
import space.astralbridge.eeg4asd.repository.TaskRepository;
import space.astralbridge.eeg4asd.repository.UserRepository;

@Service
public class TasksService {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Task> getAllTasks(HttpServletRequest request) {
        User user = userRepository.findBy_id(request.getAttribute("uid").toString());
        if ("b".equals(user.getRole())) {
            List<User> patients = userRepository.findByParentId(user.get_id());
            List<String> patientIDs = patients.stream().map(User::get_id).collect(Collectors.toList());
            return taskRepository.findByPatientIDIn(patientIDs);
        } else if ("c".equals(user.getRole())) {
            return taskRepository.findByPatientID(user.get_id());
        } else if ("a".equals(user.getRole())) {
            return taskRepository.findAll();
        }

        return taskRepository.findAll();
    }

}
