package pmf.master.platforma.camunda.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pmf.master.platforma.camunda.model.CamundaStartProcessInstanceRequestDto;
import pmf.master.platforma.camunda.model.CamundaStartProcessInstanceResponseDto;
import pmf.master.platforma.model.StartProcessInstanceRequestDto;
import pmf.master.platforma.model.StartProcessInstanceResponseDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CamundaProcessMapper {
    public static CamundaStartProcessInstanceRequestDto toCamundaStartProcessInstanceRequestDto(StartProcessInstanceRequestDto startProcessInstanceRequestDto) {
        if(startProcessInstanceRequestDto == null) {
            return null;
        }

        return new CamundaStartProcessInstanceRequestDto(
          startProcessInstanceRequestDto.getVariables(),
                startProcessInstanceRequestDto.getBusinessKey()
        );
    }

    public static StartProcessInstanceResponseDto toStartProcessInstanceResponseDto(CamundaStartProcessInstanceResponseDto camundaStartProcessInstanceResponseDto) {
        if(camundaStartProcessInstanceResponseDto == null) {
            return null;
        }

        return new StartProcessInstanceResponseDto(
          camundaStartProcessInstanceResponseDto.getVariables(),
                camundaStartProcessInstanceResponseDto.getId(),
                camundaStartProcessInstanceResponseDto.getDefinitionId(),
                camundaStartProcessInstanceResponseDto.getBusinessKey(),
                camundaStartProcessInstanceResponseDto.getCaseInstanceId(),
                camundaStartProcessInstanceResponseDto.getSuspended(),
                camundaStartProcessInstanceResponseDto.getTenantId(),
                camundaStartProcessInstanceResponseDto.getLinks()
        );
    }
}
