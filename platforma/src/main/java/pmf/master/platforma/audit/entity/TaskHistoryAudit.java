package pmf.master.platforma.audit.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pmf.master.platforma.util.Action;
import pmf.master.platforma.util.TaskState;

import java.time.LocalDateTime;

@Entity
@Table(name = "task_history_audit")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TaskHistoryAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String taskHistoryId;
    private String processInstanceHistoryId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime modifiedTime;
    private String assignee;
    private String operationUser;
    private TaskState state;
    private Action action;

    public TaskHistoryAudit(String taskHistoryId, String processInstanceHistoryId, LocalDateTime startTime, LocalDateTime endTime, LocalDateTime modifiedTime, String assignee, String operationUser, TaskState state) {
        this.taskHistoryId = taskHistoryId;
        this.processInstanceHistoryId = processInstanceHistoryId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.modifiedTime = modifiedTime;
        this.assignee = assignee;
        this.operationUser = operationUser;
        this.state = state;
    }
}
