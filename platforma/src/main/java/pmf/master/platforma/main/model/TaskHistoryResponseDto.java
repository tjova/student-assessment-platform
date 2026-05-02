package pmf.master.platforma.main.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pmf.master.platforma.util.TaskState;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TaskHistoryResponseDto {
    private String id;
    private String bpmTaskId;
    private String processInstanceHistoryId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime modifiedTime;
    private String assignee;
    private String operationUser;
    private TaskState state;
}
