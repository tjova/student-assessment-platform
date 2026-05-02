package pmf.master.platforma.main.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pmf.master.platforma.util.TaskState;

import java.time.LocalDateTime;

@Entity
@Table(name = "task_history")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TaskHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String bpmTaskId;
    private String taskName;
    private String processInstanceHistoryId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime modifiedTime;
    private String assignee;
    private String operationUser;
    private TaskState state;
}
