package pmf.master.platforma.main.entity;

import jakarta.persistence.*;
import lombok.*;
import pmf.master.platforma.util.ProcessInstanceState;

import java.time.LocalDateTime;

@Entity
@Table(name = "process_instance_history")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProcessInstanceHistory {
    @Id
    private String processInstanceHistoryId;
    private String camundaInstanceId;
    private String businessKey;
    private String processDefinitionKey;
    private ProcessInstanceState state;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime modifiedTime;
    private String processInitiatorId;
    private String operationUser;

    public ProcessInstanceHistory(String processInstanceHistoryId,
                                  String camundaInstanceId,
                                  String processDefinitionKey) {
        this.processInstanceHistoryId = processInstanceHistoryId;
        this.camundaInstanceId = camundaInstanceId;
        this.processDefinitionKey = processDefinitionKey;
    }
}
