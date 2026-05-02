package pmf.master.platforma.audit.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pmf.master.platforma.util.Action;
import pmf.master.platforma.util.ProcessInstanceState;

import java.time.LocalDateTime;

@Entity
@Table(name = "process_instance_audit")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProcessInstanceAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String processInstanceHistoryId;
    private String businessKey;
    private String processDefinitionKey;
    private ProcessInstanceState state;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime modifiedTime;
    private String processInitiatorId;
    private String operationUser;
    private Action action;

    public ProcessInstanceAudit(String processInstanceHistoryId, String businessKey, String processDefinitionKey, ProcessInstanceState state, LocalDateTime startTime, LocalDateTime endTime, LocalDateTime modifiedTime, String processInitiatorId, String operationUser) {
        this.processInstanceHistoryId = processInstanceHistoryId;
        this.businessKey = businessKey;
        this.state = state;
        this.startTime = startTime;
        this.endTime = endTime;
        this.modifiedTime = modifiedTime;
        this.processInitiatorId = processInitiatorId;
        this.operationUser = operationUser;
    }
}
