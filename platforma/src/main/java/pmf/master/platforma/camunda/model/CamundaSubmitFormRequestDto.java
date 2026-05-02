package pmf.master.platforma.camunda.model;

import lombok.*;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CamundaSubmitFormRequestDto {
    private Map<String, CamundaSubmitFormPartResponseDto> variables;
    private Boolean withVariablesInReturn = false;
}
