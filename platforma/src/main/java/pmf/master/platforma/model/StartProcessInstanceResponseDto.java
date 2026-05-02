package pmf.master.platforma.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pmf.master.platforma.camunda.model.CamundaVariableValueDto;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StartProcessInstanceResponseDto {
    private String id;
    private String definitionId;
    private String businessKey;
    private String caseInstanceId;
    private Boolean suspended;
    private String tenantId;
    private Object links;
    private Map<String, CamundaVariableValueDto> variables;

    public StartProcessInstanceResponseDto(Map<String, CamundaVariableValueDto> variables, String id, String definitionId, String businessKey, String caseInstanceId, Boolean suspended, String tenantId, Object links) {
        this.variables = variables;
        this.id = id;
        this.definitionId = definitionId;
        this.businessKey = businessKey;
        this.caseInstanceId = caseInstanceId;
        this.suspended = suspended;
        this.tenantId = tenantId;
        this.links = links;
    }
}
