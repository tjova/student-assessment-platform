package pmf.master.platforma.main.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pmf.master.platforma.util.ProcessInstanceState;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProcessInstanceHistoryResponseDto {
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
}
