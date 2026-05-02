package pmf.master.platforma.camunda.model;

import lombok.*;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CamundaSubmitFormPartResponseDto {
    private Object value;
    private String type;
    private Map<String, Object> valueInfo;
}
