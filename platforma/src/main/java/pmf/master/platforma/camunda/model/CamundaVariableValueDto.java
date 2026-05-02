package pmf.master.platforma.camunda.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CamundaVariableValueDto {
    private String value;
    private String type;
    private Object valueInfo;
}
