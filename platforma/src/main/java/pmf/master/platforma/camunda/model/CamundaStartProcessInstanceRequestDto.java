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
public class CamundaStartProcessInstanceRequestDto {
    private String businessKey;
    private Map<String, CamundaVariableValueDto> variables;
    private String caseInstanceId;
    private List<CamundaProcessInstanceModificationInstructionDto> startInstructions;
    private Boolean skipCustomListeners;
    private Boolean skipIoMappings;
    private Boolean withVariablesInReturn;

    public CamundaStartProcessInstanceRequestDto(Map<String, CamundaVariableValueDto> variables, String businessKey) {
        this.variables = variables;
        this.businessKey = businessKey;
    }
}
