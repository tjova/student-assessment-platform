package pmf.master.platforma.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;
import pmf.master.platforma.audit.entity.VariableHistoryAudit;
import pmf.master.platforma.camunda.model.CamundaSubmitFormPartResponseDto;
import pmf.master.platforma.camunda.model.CamundaVariableValueDto;
import pmf.master.platforma.main.entity.VariableHistory;
import pmf.master.platforma.main.model.VariableDto;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VariableHistoryMapper {
    public static VariableDto toVariableDto(VariableHistory variableHistory) {
        if(variableHistory == null) {
            return null;
        }

        return new VariableDto(
                variableHistory.getName(),
                variableHistory.getType(),
                variableHistory.getValue()
        );
    }

    public static List<VariableDto> toVariableDtoList(List<VariableHistory> variableHistoryList) {
        if(CollectionUtils.isEmpty(variableHistoryList)) {
            return Collections.emptyList();
        }

        List<VariableDto> variableDtoList = new ArrayList<>();

        for(VariableHistory vh : variableHistoryList) {
            variableDtoList.add(toVariableDto(vh));
        }

        return variableDtoList;
    }

    public static VariableHistory mapToVariableHistoryFromStartRequest(
            String name,
            CamundaVariableValueDto dto) {

        VariableHistory history = new VariableHistory();
        history.setId(UUID.randomUUID().toString());

        history.setName(name);
        history.setType(dto.getType());
        history.setValue(dto.getValue() != null ? dto.getValue().toString() : null);


        LocalDateTime now = LocalDateTime.now();
        history.setCreatedTime(now);
        history.setModifiedTime(now);

        return history;
    }

    public static List<VariableHistory> toVariableHistoryList(
            Map<String, CamundaVariableValueDto> variables) {

        if (variables == null) {
            return List.of();
        }

        return variables.entrySet().stream()
                .map(entry -> mapToVariableHistoryFromStartRequest(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    public static VariableHistory cloneVariable(VariableHistory variable) {
        if(variable == null) {
            return null;
        }

        VariableHistory variableHistory = new VariableHistory();
        variableHistory.setId(variable.getId());
        variableHistory.setName(variable.getName());
        variableHistory.setType(variable.getType());
        variableHistory.setValue(variable.getValue());
        variableHistory.setProcessInstanceId(variable.getProcessInstanceId());
        variableHistory.setTaskId(variable.getTaskId());
        variableHistory.setCreatedTime(variable.getCreatedTime());
        variableHistory.setModifiedTime(variable.getModifiedTime());
        return variableHistory;
    }

    public static VariableDto toVariableDtoFromCamundaDto(String name, CamundaSubmitFormPartResponseDto camundaResponse) {
        if (camundaResponse == null) {
            return null;
        }

        VariableDto dto = new VariableDto();
        dto.setName(name);
        dto.setType(camundaResponse.getType());

        // Convert Object to String safely
        if (camundaResponse.getValue() != null) {
            dto.setValue(String.valueOf(camundaResponse.getValue()));
        }

        return dto;
    }

    public static List<VariableDto> toVariableDtoListFromCamundaDto(Map<String, CamundaSubmitFormPartResponseDto> camundaResponseMap) {
        if (camundaResponseMap == null) {
            return List.of();
        }

        return camundaResponseMap.entrySet().stream()
                .map(entry -> toVariableDtoFromCamundaDto(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    public static VariableHistory toVariableHistory(VariableDto dto) {
        if (dto == null) {
            return null;
        }

        VariableHistory entity = new VariableHistory();

        // Generate a UUID for the ID field if it's new

        entity.setName(dto.getName());
        entity.setType(dto.getType());
        entity.setValue(dto.getValue());
        entity.setProcessInstanceId(dto.getProcessInstanceId());
        entity.setTaskId(dto.getTaskId());

        // Use DTO times if they exist, otherwise let the DB/App handle defaults
        entity.setCreatedTime(dto.getCreatedTime());
        entity.setModifiedTime(dto.getModifiedTime());

        return entity;
    }

    public static List<VariableHistory> toVariableHistoryList(List<VariableDto> dtos) {
        if (dtos == null) {
            return List.of();
        }

        return dtos.stream()
                .map(VariableHistoryMapper::toVariableHistory)
                .collect(Collectors.toList());
    }

    public static VariableHistoryAudit toAudit(VariableHistory history) {
        if (history == null) {
            return null;
        }

        VariableHistoryAudit audit = new VariableHistoryAudit();

        audit.setVariableHistoryId(history.getId());
        audit.setName(history.getName());
        audit.setType(history.getType());
        audit.setValue(history.getValue());
        audit.setProcessInstanceId(history.getProcessInstanceId());
        audit.setTaskId(history.getTaskId());
        audit.setCreatedTime(history.getCreatedTime());
        audit.setModifiedTime(history.getModifiedTime());

        return audit;
    }

    public static List<VariableHistoryAudit> toAuditList(List<VariableHistory> histories) {
        if (histories == null) {
            return List.of();
        }

        return histories.stream()
                .map(VariableHistoryMapper::toAudit)
                .collect(Collectors.toList());
    }
}
