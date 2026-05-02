package pmf.master.platforma.camunda.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CamundaStartProcessInstanceResponseDto {
    private Map<String, CamundaVariableValueDto> variables;
    private String id;
    private String definitionId;
    private String businessKey;
    private String caseInstanceId;
    private Boolean suspended;
    private String tenantId;
    private List<CamundaAtomLinkDto> links;
}
