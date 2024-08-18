package space.astralbridge.eeg4asd.service.bussine;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import space.astralbridge.eeg4asd.dto.request.CreateTaskRequestDTO;
import space.astralbridge.eeg4asd.dto.request.GetTaskRequestDTO;
import space.astralbridge.eeg4asd.dto.request.PutTaskRequestDTO;
import space.astralbridge.eeg4asd.dto.response.CreateTaskResponseDTO;
import space.astralbridge.eeg4asd.dto.response.GetTaskResponseDTO;
import space.astralbridge.eeg4asd.dto.response.PutTaskResponseDTO;
import space.astralbridge.eeg4asd.model.Task;
import space.astralbridge.eeg4asd.repository.TaskRepository;
import space.astralbridge.eeg4asd.service.dto.MessageRequest;
import space.astralbridge.eeg4asd.service.mq.MessageService;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final MessageService messageService;

    @Autowired
    public TaskService(TaskRepository taskRepository, MessageService messageService) {
        this.taskRepository = taskRepository;
        this.messageService = messageService;
    }

    public CreateTaskResponseDTO createTask(CreateTaskRequestDTO reqDTO) throws IOException {
        Task task = new Task();

        task.setState("pending");

        task.setEegFilePath(saveFile(reqDTO.getEegFile(), task.get_id()));

        task.setPatientID(reqDTO.getPatientID());

        messageService.sendRequest(new MessageRequest(task.get_id(), task.getEegFilePath()));

        task.setUpdatedAt(Instant.now());

        taskRepository.save(task);

        return new CreateTaskResponseDTO(task.get_id());
    }

    public GetTaskResponseDTO getTask(GetTaskRequestDTO requestDTO) {
        Task task = taskRepository.findById(requestDTO.getId())
                .orElseThrow(() -> new NoSuchElementException("Document Not Found"));

        GetTaskResponseDTO resDTO = new GetTaskResponseDTO();

        resDTO.set_id(task.get_id());

        if (requestDTO.isEegFilePath())
            resDTO.setEegFilePath(task.getEegFilePath());

        if (requestDTO.isPatientID()) {
            resDTO.setPatientID(task.getPatientID());
        }

        if (requestDTO.isState())
            resDTO.setState(task.getState());

        if (requestDTO.isResult())
            resDTO.setResult(task.getResult());

        return resDTO;

    }

    public PutTaskResponseDTO putTask(PutTaskRequestDTO requestDTO) {
        // logger.info("Request: {}", requestDTO);

        Task task = taskRepository.findById(requestDTO.get_id())
                .orElseThrow(() -> new NoSuchElementException("Document Not Found"));

        if (requestDTO.eegFilePath != null)
            task.setEegFilePath(requestDTO.getEegFilePath());

        if (requestDTO.patientID != null)
            task.setPatientID(requestDTO.getPatientID());

        if (requestDTO.state != null)
            task.setState(requestDTO.getState());

        if (requestDTO.result != null)
            task.setResult(requestDTO.getResult());

        task.setUpdatedAt(Instant.now());

        taskRepository.save(task);

        return new PutTaskResponseDTO(task.get_id());
    }

    public void deleteTask(String taskId) {
        taskRepository.deleteById(taskId);
    }

    private String saveFile(MultipartFile file, String objId) throws IOException {

        String baseDir = System.getProperty("java.io.tmpdir");

        String subDirPath = Paths.get(baseDir, "eeg4asd", "uploads", objId).toString();

        File subDir = new File(subDirPath);
        if (!subDir.exists()) {
            boolean dirsCreated = subDir.mkdirs();
            if (!dirsCreated) {
                throw new IOException("Failed to create directories: " + subDirPath);
            }
        }
        File targetFile = new File(subDir, objId + ".mat");

        file.transferTo(targetFile);

        return subDirPath;
    }

}
