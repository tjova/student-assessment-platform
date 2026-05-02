package pmf.master.platforma.validator;

import io.micrometer.common.util.StringUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pmf.master.platforma.exceptionHandling.BadRequestException;
import pmf.master.platforma.main.entity.TaskHistory;
import pmf.master.platforma.main.repository.TaskHistoryRepository;
import pmf.master.platforma.util.TaskState;

import java.util.Optional;

@Service
@AllArgsConstructor
public class TaskValidator {

    private final TaskHistoryRepository taskHistoryRepository;

    public void validateTaskId(String taskId, String errorMessage) {
        if(StringUtils.isBlank(taskId)) {
            throw new BadRequestException(errorMessage);
        }
    }

    public void validateTaskClaimed(String taskId) {
        Optional<TaskHistory> taskHistory = taskHistoryRepository.findById(taskId);
        if(taskHistory.isPresent()) {
            if (taskHistory.get().getAssignee() != null) {
                throw new BadRequestException(String.format("Task with id %s has already been claimed by another user", taskId));
            }
        }
        else {
            throw new BadRequestException(String.format("Task with id %s does not exist", taskId));
        }
    }

    public void validateTaskNotClaimed(String taskId) {
        Optional<TaskHistory> taskHistory = taskHistoryRepository.findById(taskId);
        if(taskHistory.isPresent()) {
            if (taskHistory.get().getAssignee() == null) {
                throw new BadRequestException(String.format("Task with id %s has not been claimed by any user", taskId));
            }
        }
        else {
            throw new BadRequestException(String.format("Task with id %s does not exist", taskId));
        }
    }

    public void validateTaskComplete(String taskId) {
        Optional<TaskHistory> taskHistory = taskHistoryRepository.findById(taskId);
        if(taskHistory.isPresent()) {
            if (TaskState.COMPLETED.equals(taskHistory.get().getState())) {
                throw new BadRequestException(String.format("Task with id %s has already been completed", taskId));
            }
        }else {
            throw new BadRequestException(String.format("Task with id %s does not exist", taskId));
        }
    }
}
