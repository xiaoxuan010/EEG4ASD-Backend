package space.astralbridge.eeg4asd.service.bussine;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.NoSuchElementException;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

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

    public String getTaskPredictionResultDescribtion(String taskId)
            throws JsonMappingException, JsonProcessingException {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NoSuchElementException("Document Not Found"));

        Integer severityNumber = null;
        String keyNumber = null;
        String keyTime = null;

        String predictionResultJson = task.getResult();
        if (predictionResultJson != null) {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = null;

            json = mapper.readTree(predictionResultJson);

            severityNumber = json.get("severity_number").asInt();
            keyNumber = json.get("key_number").asText();
            keyTime = json.get("key_time").asText();
        }

        String severityDescption = null;
        if (severityNumber == null) {
            throw new RuntimeErrorException(null, "severityNumber is null");
        }
        switch (severityNumber) {
            case 1:
                severityDescption = "正常";
                break;

            case 2:
                severityDescption = "1级ASD";
                break;

            case 3:
                severityDescption = "2级ASD";
                break;

            case 4:
                severityDescption = "3级ASD";
                break;
            default:
                throw new RuntimeErrorException(null, "severityNumber is invalid");
        }

        String resultDescription = "自闭症严重等级为" + severityDescption + "；病情预计在" + keyNumber + "小时后高发：病情在一天中的高发时段为"
                + keyTime + "。";

        return resultDescription;

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
