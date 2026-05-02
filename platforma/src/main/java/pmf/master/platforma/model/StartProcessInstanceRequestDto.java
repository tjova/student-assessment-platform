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
public class StartProcessInstanceRequestDto {
    private String processDefinitionKey;
    private String processDefinitionId;
    private Integer processDefinitionVersion;
    private String businessKey;
    private Map<String, CamundaVariableValueDto> variables;
}
