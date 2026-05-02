package pmf.master.platforma.main.service;

import io.micrometer.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pmf.master.platforma.audit.service.ProcessInstanceAuditService;
import pmf.master.platforma.camunda.model.CamundaHttpConnectorRequest;
import pmf.master.platforma.camunda.service.CamundaProcessInstanceService;
import pmf.master.platforma.exceptionHandling.BadRequestException;
import pmf.master.platforma.exceptionHandling.NotFoundException;
import pmf.master.platforma.main.entity.ProcessInstanceHistory;
import pmf.master.platforma.main.entity.TaskHistory;
import pmf.master.platforma.main.entity.VariableHistory;
import pmf.master.platforma.main.model.ProcessInstanceHistoryResponseDto;
import pmf.master.platforma.main.repository.ProcessInstanceHistoryRepository;
import pmf.master.platforma.main.repository.TaskHistoryRepository;
import pmf.master.platforma.main.repository.VariableHistoryRepository;
import pmf.master.platforma.mapper.VariableHistoryMapper;
import pmf.master.platforma.model.StartProcessInstanceRequestDto;
import pmf.master.platforma.model.StartProcessInstanceResponseDto;
import pmf.master.platforma.mapper.ProcessInstanceHistoryMapper;
import pmf.master.platforma.util.Action;
import pmf.master.platforma.util.CamundaVariableNames;
import pmf.master.platforma.util.ProcessInstanceState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProcessInstanceService {

    //todo add logging !!! refactor once variables are introduced

    private final CamundaProcessInstanceService camundaProcessInstanceService;
    private final ProcessInstanceHistoryRepository processInstanceHistoryRepository;
    private final ProcessInstanceAuditService processInstanceAuditService;
    private final TaskHistoryRepository taskHistoryRepository;
    private final VariableHistoryRepository variableHistoryRepository;
    private final Logger logger = LoggerFactory.getLogger(ProcessInstanceService.class);

    public ProcessInstanceService(CamundaProcessInstanceService camundaProcessInstanceService,
                                  ProcessInstanceHistoryRepository processInstanceHistoryRepository,
                                  TaskHistoryRepository taskHistoryRepository,
                                  ProcessInstanceAuditService processInstanceAuditService,
                                  VariableHistoryRepository variableHistoryRepository) {
        this.camundaProcessInstanceService = camundaProcessInstanceService;
        this.processInstanceHistoryRepository = processInstanceHistoryRepository;
        this.taskHistoryRepository = taskHistoryRepository;
        this.processInstanceAuditService = processInstanceAuditService;
        this.variableHistoryRepository = variableHistoryRepository;
    }

    @Transactional
    public StartProcessInstanceResponseDto startProcessInstance(StartProcessInstanceRequestDto requestDto) {
        StartProcessInstanceResponseDto processInstanceResponse = camundaProcessInstanceService.startProcessInstance(requestDto.getProcessDefinitionId(), requestDto);
        if(processInstanceResponse == null) {
            throw new BadRequestException("An error occurred while starting process instance in Camunda");
        }
        ProcessInstanceHistory processInstanceHistory = ProcessInstanceHistoryMapper.toProcessInstanceHistory(processInstanceResponse);
        ProcessInstanceHistory processInstanceHistoryUpdated = setAdditionalPIHParameters(processInstanceHistory);
        List<VariableHistory> savedVariables = variableHistoryRepository.saveAll(VariableHistoryMapper.toVariableHistoryList(requestDto.getVariables()));
        ProcessInstanceHistory processInstanceHistorySaved = processInstanceHistoryRepository.save(processInstanceHistoryUpdated);
        processInstanceAuditService.saveProcessInstanceAudit(processInstanceHistorySaved, Action.INSTANCE_CREATE);
        logger.info(String.format("Successfully started process instance for definition %s", requestDto.getProcessDefinitionId()));
        return processInstanceResponse;
    }

    private ProcessInstanceHistory setAdditionalPIHParameters(ProcessInstanceHistory processInstanceHistory) {
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        LocalDateTime currentTime = LocalDateTime.now();

        processInstanceHistory.setProcessInitiatorId(username);
        processInstanceHistory.setOperationUser(username);
        processInstanceHistory.setStartTime(currentTime);
        processInstanceHistory.setModifiedTime(currentTime);

        return processInstanceHistory;
    }

    @Transactional
    public void completeProcessInstance(CamundaHttpConnectorRequest request) {
        if(request == null) {
            throw new BadRequestException("Camunda request for complete process instance is null");
        }

        Optional<ProcessInstanceHistory> processInstanceHistory = processInstanceHistoryRepository.findById(request.getVariables().get(request.getVariables().get(CamundaVariableNames.PROCESS_INSTANCE_ID)));
        if(processInstanceHistory.isEmpty()) {
            throw new NotFoundException(String.format("Process instance history with id %s does not exist", request.getVariables().get(request.getVariables().get(CamundaVariableNames.PROCESS_INSTANCE_ID))));
        }

        processInstanceHistory.get().setModifiedTime(LocalDateTime.now());
        processInstanceHistory.get().setEndTime(LocalDateTime.now());
        processInstanceHistory.get().setState(ProcessInstanceState.COMPLETED);

        Optional<TaskHistory> lastTask = taskHistoryRepository.findFirstByProcessInstanceHistoryIdAndEndTimeNotNullOrderByEndTimeDesc(processInstanceHistory.get().getProcessInstanceHistoryId());
        lastTask.ifPresent(taskHistory -> processInstanceHistory.get().setOperationUser(taskHistory.getOperationUser()));
        ProcessInstanceHistory savedProcessInstanceHistory = processInstanceHistoryRepository.save(processInstanceHistory.get());
        processInstanceAuditService.saveProcessInstanceAudit(savedProcessInstanceHistory, Action.INSTANCE_COMPLETE);
        logger.info(String.format("Successfully completed process instance with id %s", savedProcessInstanceHistory.getProcessInstanceHistoryId()));
    }

    @Transactional(readOnly = true)
    public ProcessInstanceHistoryResponseDto getProcessInstanceHistory(String processInstanceHistoryId) {
        if(StringUtils.isEmpty(processInstanceHistoryId)) {
            throw new BadRequestException("Process instance history cannot be null or empty");
        }
        Optional<ProcessInstanceHistory> processInstanceHistoryOptional = processInstanceHistoryRepository.findById(processInstanceHistoryId);
        if(processInstanceHistoryOptional.isEmpty()) {
            throw new NotFoundException(String.format("Process Instance History with id %s does not exist", processInstanceHistoryId));
        }
        ProcessInstanceHistory processInstanceHistory = processInstanceHistoryOptional.get();
        return ProcessInstanceHistoryMapper.toProcessInstanceHistoryResponseDto(processInstanceHistory);
    }
}
