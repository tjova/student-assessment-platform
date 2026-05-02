package pmf.master.platforma.camunda.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pmf.master.platforma.camunda.mapper.CamundaProcessMapper;
import pmf.master.platforma.camunda.proxy.CamundaProcessInstanceProxy;
import pmf.master.platforma.model.StartProcessInstanceRequestDto;
import pmf.master.platforma.model.StartProcessInstanceResponseDto;

@Service
public class CamundaProcessInstanceService {

    private final CamundaProcessInstanceProxy camundaProcessInstanceProxy;
    private final Logger logger = LoggerFactory.getLogger(CamundaProcessInstanceProxy.class);

    public CamundaProcessInstanceService(CamundaProcessInstanceProxy camundaProcessInstanceProxy) {
        this.camundaProcessInstanceProxy = camundaProcessInstanceProxy;
    }

    public StartProcessInstanceResponseDto startProcessInstance(String id, StartProcessInstanceRequestDto startProcessInstanceRequestDto) {
        logger.info(String.format("Starting process instance with id %s in Camunda", id));
        return CamundaProcessMapper.toStartProcessInstanceResponseDto(camundaProcessInstanceProxy.startProcessInstanceById
                (id, CamundaProcessMapper.toCamundaStartProcessInstanceRequestDto(startProcessInstanceRequestDto)));
    }
}
