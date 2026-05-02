package pmf.master.platforma.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pmf.master.platforma.audit.entity.ProcessInstanceAudit;
import pmf.master.platforma.main.entity.ProcessInstanceHistory;
import pmf.master.platforma.main.model.ProcessInstanceHistoryResponseDto;
import pmf.master.platforma.model.StartProcessInstanceResponseDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProcessInstanceHistoryMapper {
    public static ProcessInstanceHistory toProcessInstanceHistory(StartProcessInstanceResponseDto startProcessInstanceResponseDto) {
        if(startProcessInstanceResponseDto == null) {
            return null;
        }

        return new ProcessInstanceHistory(
                startProcessInstanceResponseDto.getId(),
                startProcessInstanceResponseDto.getCaseInstanceId(),
                startProcessInstanceResponseDto.getDefinitionId()
        );
    }

    public static ProcessInstanceHistoryResponseDto toProcessInstanceHistoryResponseDto(ProcessInstanceHistory processInstanceHistory) {
        if(processInstanceHistory == null) {
            return null;
        }

        return new ProcessInstanceHistoryResponseDto(
          processInstanceHistory.getProcessInstanceHistoryId(),
          processInstanceHistory.getCamundaInstanceId(),
                processInstanceHistory.getBusinessKey(),
                processInstanceHistory.getProcessDefinitionKey(),
                processInstanceHistory.getState(),
                processInstanceHistory.getStartTime(),
                processInstanceHistory.getEndTime(),
                processInstanceHistory.getModifiedTime(),
                processInstanceHistory.getProcessInitiatorId(),
                processInstanceHistory.getOperationUser()
        );
    }

    public static ProcessInstanceAudit toProcessInstanceAudit(ProcessInstanceHistory processInstanceHistory) {
        if(processInstanceHistory == null) {
            return null;
        }

        return new ProcessInstanceAudit(
          processInstanceHistory.getProcessInstanceHistoryId(),
                processInstanceHistory.getBusinessKey(),
                processInstanceHistory.getProcessDefinitionKey(),
                processInstanceHistory.getState(),
                processInstanceHistory.getStartTime(),
                processInstanceHistory.getEndTime(),
                processInstanceHistory.getModifiedTime(),
                processInstanceHistory.getProcessInitiatorId(),
                processInstanceHistory.getOperationUser()
        );
    }
}
