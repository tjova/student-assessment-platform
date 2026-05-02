package pmf.master.platforma.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pmf.master.platforma.audit.entity.TaskHistoryAudit;
import pmf.master.platforma.main.entity.TaskHistory;
import pmf.master.platforma.main.model.TaskHistoryResponseDto;
import pmf.master.platforma.util.CamundaVariableNames;
import pmf.master.platforma.util.TaskState;

import java.time.LocalDateTime;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TaskHistoryMapper {
    public static TaskHistoryResponseDto toTaskHistoryResponseDto(TaskHistory taskHistory) {
        if(taskHistory == null) {
            return null;
        }

        return new TaskHistoryResponseDto(
          taskHistory.getId(),
          taskHistory.getBpmTaskId(),
          taskHistory.getProcessInstanceHistoryId(),
          taskHistory.getStartTime(),
          taskHistory.getEndTime(),
          taskHistory.getModifiedTime(),
          taskHistory.getAssignee(),
          taskHistory.getOperationUser(),
          taskHistory.getState()
        );
    }

    public static TaskHistory toTaskHistoryFromConnectorVariables(Map<String, String> variables) {
        if(variables == null) {
            return null;
        }

        TaskHistory taskHistory = new TaskHistory();
        taskHistory.setBpmTaskId(variables.get("bpmTaskId"));
        taskHistory.setTaskName(variables.get(CamundaVariableNames.TASK_NAME));
        taskHistory.setProcessInstanceHistoryId(variables.get(CamundaVariableNames.PROCESS_INSTANCE_ID));
        taskHistory.setStartTime(LocalDateTime.now());
        taskHistory.setState(TaskState.ACTIVE);
        taskHistory.setAssignee(variables.get(CamundaVariableNames.ASSIGNEE));

        return taskHistory;
    }

    public static TaskHistoryAudit toTaskHistoryAudit(TaskHistory taskHistory) {
        if(taskHistory == null) {
            return null;
        }

        return new TaskHistoryAudit(
                taskHistory.getBpmTaskId(),
                taskHistory.getProcessInstanceHistoryId(),
                taskHistory.getStartTime(),
                taskHistory.getEndTime(),
                taskHistory.getModifiedTime(),
                taskHistory.getAssignee(),
                taskHistory.getOperationUser(),
                taskHistory.getState()
        );
    }
}
