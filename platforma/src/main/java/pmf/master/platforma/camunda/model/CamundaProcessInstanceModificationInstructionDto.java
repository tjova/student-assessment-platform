package pmf.master.platforma.camunda.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pmf.master.platforma.camunda.util.InstructionType;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CamundaProcessInstanceModificationInstructionDto {
    private InstructionType instructionType;
    private Map<String, CamundaTriggerVariableValueDto> variables;
    private String activityId;
    private String transitionId;
    private String activityInstanceId;
    private String transitionInstanceId;
    private String ancestorActivityId;
    private Boolean cancelCurrentActiveInstances;
}
