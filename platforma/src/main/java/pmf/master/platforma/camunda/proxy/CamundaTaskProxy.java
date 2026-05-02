package pmf.master.platforma.camunda.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import pmf.master.platforma.camunda.config.CamundaFeignConfig;
import pmf.master.platforma.camunda.model.CamundaClaimTaskRequestDto;
import pmf.master.platforma.camunda.model.CamundaSubmitFormPartResponseDto;
import pmf.master.platforma.camunda.model.CamundaSubmitFormRequestDto;

import org.springframework.web.bind.annotation.*;
import java.util.Map;
@FeignClient(
        name = "camunda-engine",
        url = "http://localhost:8080/engine-rest",
        configuration =
        CamundaFeignConfig.class
)
public interface CamundaTaskProxy {

    @PostMapping(
            value = "task/{id}/submit-form")
    Map<String, CamundaSubmitFormPartResponseDto> submitForm(
            @PathVariable("id") String taskId,
            @RequestBody CamundaSubmitFormRequestDto requestDto
    );

    @PostMapping("task/{id}/claim")
    void claimTask(@PathVariable("id") String id, @RequestBody CamundaClaimTaskRequestDto requestDto);

    @PostMapping("task/{id}/unclaim")
    void unclaimTask(@PathVariable("id") String id);

    @PostMapping("task/{id}/complete")
    void completeTask(@PathVariable("id") String id, @RequestBody CamundaSubmitFormRequestDto requestDto);
}
