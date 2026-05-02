package pmf.master.platforma.camunda.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pmf.master.platforma.camunda.model.CamundaStartProcessInstanceRequestDto;
import pmf.master.platforma.camunda.model.CamundaStartProcessInstanceResponseDto;
import pmf.master.platforma.camunda.config.CamundaFeignConfig;

@FeignClient(value="camundaProcessInstanceProxy",
        url="http://localhost:8080/engine-rest",
        configuration = CamundaFeignConfig.class)
public interface CamundaProcessInstanceProxy {

    @PostMapping("/process-definition/{id}/start")
    CamundaStartProcessInstanceResponseDto startProcessInstanceById(@PathVariable String id, @RequestBody CamundaStartProcessInstanceRequestDto requestDto);
}