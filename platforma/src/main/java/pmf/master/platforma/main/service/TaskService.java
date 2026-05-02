package pmf.master.platforma.main.service;
import io.micrometer.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import pmf.master.platforma.audit.service.ProcessInstanceAuditService;
import pmf.master.platforma.audit.service.TaskHistoryAuditService;
import pmf.master.platforma.audit.service.VariableHistoryAuditService;
import pmf.master.platforma.camunda.model.CamundaClaimTaskRequestDto;
import pmf.master.platforma.camunda.model.CamundaSubmitFormPartResponseDto;
import pmf.master.platforma.camunda.model.CamundaSubmitFormRequestDto;
import pmf.master.platforma.camunda.proxy.CamundaTaskProxy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pmf.master.platforma.exceptionHandling.BadRequestException;
import pmf.master.platforma.exceptionHandling.NotFoundException;
import pmf.master.platforma.main.entity.ProcessInstanceHistory;
import pmf.master.platforma.main.entity.TaskHistory;
import pmf.master.platforma.main.entity.VariableHistory;
import pmf.master.platforma.main.model.TaskHistoryResponseDto;
import pmf.master.platforma.main.model.VariableDto;
import pmf.master.platforma.main.repository.ProcessInstanceHistoryRepository;
import pmf.master.platforma.main.repository.TaskHistoryRepository;
import pmf.master.platforma.main.repository.VariableHistoryRepository;
import pmf.master.platforma.mapper.TaskHistoryMapper;
import pmf.master.platforma.mapper.VariableHistoryMapper;
import pmf.master.platforma.util.Action;
import pmf.master.platforma.util.TaskState;
import pmf.master.platforma.validator.TaskValidator;
import pmf.master.platforma.validator.VariableValidator;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service("CustomTaskService")
public class TaskService {

    private final CamundaTaskProxy camundaTaskProxy;
    private final TaskValidator taskValidator;
    private final TaskHistoryRepository taskHistoryRepository;
    private final TaskHistoryAuditService taskHistoryAuditService;
    private final VariableHistoryRepository variableHistoryRepository;
    private final VariableValidator variableValidator;
    private final VariableHistoryAuditService variableHistoryAuditService;
    private final ProcessInstanceHistoryRepository processInstanceHistoryRepository;
    private final ProcessInstanceAuditService processInstanceAuditService;
    private final Logger logger = LoggerFactory.getLogger(TaskService.class);

    public TaskService(CamundaTaskProxy camundaTaskProxy,
                       TaskValidator taskValidator,
                       TaskHistoryRepository taskHistoryRepository,
                       TaskHistoryAuditService taskHistoryAuditService,
                       VariableHistoryRepository variableHistoryRepository,
                       VariableValidator variableValidator,
                       VariableHistoryAuditService variableHistoryAuditService,
                       ProcessInstanceHistoryRepository processInstanceHistoryRepository,
                       ProcessInstanceAuditService processInstanceAuditService) {
        this.camundaTaskProxy = camundaTaskProxy;
        this.taskValidator = taskValidator;
        this.taskHistoryRepository = taskHistoryRepository;
        this.taskHistoryAuditService = taskHistoryAuditService;
        this.variableHistoryRepository = variableHistoryRepository;
        this.variableValidator = variableValidator;
        this.variableHistoryAuditService = variableHistoryAuditService;
        this.processInstanceHistoryRepository = processInstanceHistoryRepository;
        this.processInstanceAuditService = processInstanceAuditService;
    }

    @Transactional
    public void completeTaskWithFile(String taskId, MultipartFile file, CamundaSubmitFormRequestDto requestDto) {
        taskValidator.validateTaskId(taskId, "Task ID must not be null to complete task with file");

        if (file == null) {
            throw new BadRequestException(String.format("File must be provided in order for task with ID %s to be completed", taskId));
        }

        logger.info(String.format("Started completing task with file for task with id %s", taskId));

        try {
            byte[] fileBytes = file.getBytes();
            String base64Content = Base64.getEncoder().encodeToString(fileBytes);

            Map<String, Object> valueInfoMap = new HashMap<>();
            valueInfoMap.put("filename", "slika");

            requestDto.getVariables().put("uploadedFile", new CamundaSubmitFormPartResponseDto(base64Content, "File", valueInfoMap));

            camundaTaskProxy.submitForm(taskId, requestDto);
            logger.info("Successfully completed task with file in Camunda");

            LocalDateTime now = LocalDateTime.now();
            Optional<TaskHistory> taskHistory = taskHistoryRepository.findById(taskId);
            updateTaskDataForCompletion(taskId, taskHistory.get());

            auditTaskAction(taskHistory.get(), Action.TASK_COMPLETE, now);

        } catch (IOException e) {
            logger.error(String.format("An error occurred while processing file for task completion %s", e));
            throw new RuntimeException("Error processing file for task submission.", e);
        }
    }

    private static void updateTaskDataForCompletion(String taskId, TaskHistory taskHistory) {
        if(taskHistory == null) {
            throw new NotFoundException(String.format("Task with ID %s has not been found", taskId));
        }
        taskHistory.setModifiedTime(LocalDateTime.now());
        taskHistory.setEndTime(LocalDateTime.now());
        // taskHistory.get().setOperationUser(secretService.getUsername);
        taskHistory.setState(TaskState.COMPLETED);
    }

    @Transactional
    public void claimTask(String taskId, CamundaClaimTaskRequestDto camundaClaimTaskRequestDto) {
        taskValidator.validateTaskId(taskId, "Task ID must not be null to claim task");
        taskValidator.validateTaskClaimed(taskId);
        if(StringUtils.isBlank(camundaClaimTaskRequestDto.getUserId())) {
            throw new BadRequestException("User ID must not be null to claim task");
        }
        camundaTaskProxy.claimTask(taskId, camundaClaimTaskRequestDto);
        TaskHistory taskHistory = updateTaskAssignee(taskId, camundaClaimTaskRequestDto.getUserId());
        auditTaskAction(taskHistory, Action.CLAIM, LocalDateTime.now());
        logger.info(String.format("Successfully claimed task %s for user %s", taskId, camundaClaimTaskRequestDto.getUserId()));
    }

    @Transactional
    public void unclaimTask(String taskId) {
        taskValidator.validateTaskId(taskId, "Task ID must not be null to unclaim task");
        taskValidator.validateTaskNotClaimed(taskId);
        camundaTaskProxy.unclaimTask(taskId);
        TaskHistory taskHistory = updateTaskAssignee(taskId, null);
        auditTaskAction(taskHistory, Action.UNCLAIM, LocalDateTime.now());
        logger.info(String.format("Successfully unclaimed task %s", taskId));
    }

    private TaskHistory updateTaskAssignee(String taskId, String assignee) {
        Optional<TaskHistory> taskHistory = taskHistoryRepository.findById(taskId);
        if(taskHistory.isEmpty()) {
            throw new NotFoundException(String.format("Task with ID %s does not exist", taskId));
        }
        taskHistory.get().setAssignee(assignee);
        taskHistoryRepository.save(taskHistory.get());
        return taskHistory.get();
    }

    @Transactional
    public void completeTask(String taskId, CamundaSubmitFormRequestDto requestDto) {
        taskValidator.validateTaskId(taskId, "Task ID must not be null to complete task");
        taskValidator.validateTaskComplete(taskId);

        Optional<TaskHistory> taskHistoryOptional = taskHistoryRepository.findById(taskId);
        if (taskHistoryOptional.isEmpty()) {
            throw new NotFoundException(String.format("Task with ID %s has not been found", taskId));
        }
        if (CollectionUtils.isEmpty(requestDto.getVariables())) {
            taskHistoryAuditService.saveTaskHistoryAuditHistory(taskHistoryOptional.get(), Action.TASK_COMPLETE);
        }

        Map<String, VariableHistory> existingVariables = variableHistoryRepository.findByProcessInstanceIdOrderByNameAsc
                        (taskHistoryOptional.get().getProcessInstanceHistoryId())
                .stream().collect(Collectors.toMap(VariableHistory::getName, variableHistory -> variableHistory));
        List<VariableHistory> updatedVariableForSave = new ArrayList<>();
        List<VariableHistory> originalUpdatedVariables = new ArrayList<>();
        List<VariableHistory> newVariablesForSave = new ArrayList<>();

        updateExistingAndAddNewVariables(VariableHistoryMapper.toVariableDtoListFromCamundaDto(requestDto.getVariables()), LocalDateTime.now(), existingVariables, updatedVariableForSave,
                originalUpdatedVariables, VariableHistoryMapper.toVariableDtoList(newVariablesForSave));
        saveNewVariables(taskHistoryOptional.get(), LocalDateTime.now(), VariableHistoryMapper.toVariableDtoList(newVariablesForSave), existingVariables, updatedVariableForSave);

        auditUnchangedVariables(VariableHistoryMapper.toVariableDtoListFromCamundaDto(requestDto.getVariables()),
                Action.TASK_COMPLETE,
                existingVariables
        );

        camundaTaskProxy.completeTask(taskId, requestDto);
        updateTaskDataForCompletion(taskId, taskHistoryOptional.get());
        auditTaskAction(taskHistoryOptional.get(), Action.TASK_COMPLETE, LocalDateTime.now());
        logger.info(String.format("Successfully completed task %s", taskId));
    }

    @Transactional(readOnly = true)
    public TaskHistoryResponseDto getTask(String taskId) {
        if(StringUtils.isEmpty(taskId)) {
            throw new BadRequestException("Task id cannot not be null or empty");
        }
        Optional<TaskHistory> taskHistoryOptional = taskHistoryRepository.findById(taskId);
        if(taskHistoryOptional.isEmpty()) {
            throw new NotFoundException(String.format("Task with id %s does not exist", taskId));
        }
        TaskHistory taskHistory = taskHistoryOptional.get();
        return TaskHistoryMapper.toTaskHistoryResponseDto(taskHistory);
    }

    private void updateExistingAndAddNewVariables(List<VariableDto> variables,
                                                  LocalDateTime modifiedTime,
                                                  Map<String, VariableHistory> existingVariables,
                                                  List<VariableHistory> originalUpdatedVariables,
                                                  List<VariableHistory> updatedVariablesForSave,
                                                  List<VariableDto> newVariablesForSave) {
        variables.forEach(variableDto -> {
                    if (existingVariables.containsKey(variableDto.getName())) {
                        VariableHistory existingVariable = existingVariables.get(variableDto.getName());
                        if (!existingVariable.getType().equals(variableDto.getType())) {
                            throw new BadRequestException("Change of variable type is not allowed");
                        }

                        originalUpdatedVariables.add(VariableHistoryMapper.cloneVariable(existingVariable));
                        existingVariable.setValue(variableDto.getValue());
                        existingVariable.setModifiedTime(modifiedTime);
                        updatedVariablesForSave.add(existingVariable);
                    } else {
                        newVariablesForSave.add(variableDto);
                    }
                }

        );
    }

    private void saveNewVariables(TaskHistory task, LocalDateTime modifiedTime, List<VariableDto> newVariablesForSave, Map<String, VariableHistory> existingVariables, List<VariableHistory> updatedVariablesForSave) {
        List<VariableHistory> newVariables = VariableHistoryMapper.toVariableHistoryList(newVariablesForSave);
        checkDuplicateVariables(existingVariables, newVariables);
        updatedVariablesForSave.addAll(newVariables);
        List<VariableHistory> newSavedVariables = variableHistoryRepository.saveAll(updatedVariablesForSave);
        newSavedVariables.removeAll(updatedVariablesForSave);
    }

    private void auditUnchangedVariables(List<VariableDto> variables, Action action, Map<String, VariableHistory> existingVariables) {
        List<VariableHistory> unchangedVariables = new ArrayList<>();
        List<String> variablesNames = variables.stream().map(VariableDto::getName).collect(Collectors.toList());
        existingVariables.keySet().forEach(variableName -> {
            if (!variablesNames.contains(variableName)) {
                unchangedVariables.add(existingVariables.get(variableName));
            }
        });
        variableHistoryAuditService.saveAuditVariables(unchangedVariables, action, false);
    }

    private void checkDuplicateVariables(Map<String, VariableHistory> existingVariables, List<VariableHistory> newVariables) {
        List<VariableHistory> existingVariablesList = new ArrayList<>(existingVariables.values());
        existingVariablesList.addAll(newVariables);
        variableValidator.checkForDuplicateVarNames(existingVariablesList);
    }

    private void auditTaskAction(TaskHistory taskHistory, Action action,
                                 LocalDateTime modifiedTime) {
        taskHistoryAuditService.saveTaskHistoryAuditHistory(taskHistory, Action.TASK_COMPLETE);
        ProcessInstanceHistory pi = getAndUpdateProcessInstance(taskHistory, modifiedTime);
        processInstanceAuditService.saveProcessInstanceAudit(pi, action);
        //variableHistoryService.auditVariables(variables, pidIndex, action, modified);
    }

    private ProcessInstanceHistory getAndUpdateProcessInstance(TaskHistory task, LocalDateTime modifiedTime) {
        ProcessInstanceHistory pi = processInstanceHistoryRepository.findById(task.getProcessInstanceHistoryId())
                .orElseThrow(() -> new BadRequestException("processInstance not found"));
        pi.setModifiedTime(modifiedTime);
        // pi.setModifiedBy(secretService.getUsername());
        processInstanceHistoryRepository.save(pi);
        return pi;
    }
}
