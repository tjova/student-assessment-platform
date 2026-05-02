package pmf.master.platforma.main.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import pmf.master.platforma.audit.service.ProcessInstanceAuditService;
import pmf.master.platforma.audit.service.TaskHistoryAuditService;
import pmf.master.platforma.camunda.model.CamundaHttpConnectorRequest;
import pmf.master.platforma.main.entity.ProcessInstanceHistory;
import pmf.master.platforma.main.entity.TaskHistory;
import pmf.master.platforma.main.repository.ProcessInstanceHistoryRepository;
import pmf.master.platforma.main.repository.TaskHistoryRepository;
import pmf.master.platforma.mapper.TaskHistoryMapper;
import pmf.master.platforma.util.Action;
import pmf.master.platforma.util.CamundaVariableNames;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

@Service
public class TaskAsyncService {

    private final Logger logger = LoggerFactory.getLogger(TaskAsyncService.class);
    private final TaskHistoryRepository taskHistoryRepository;
    private final ProcessInstanceHistoryRepository processInstanceHistoryRepository;
    private final ProcessInstanceAuditService processInstanceAuditService;
    private final TaskHistoryAuditService taskHistoryAuditService;

    public TaskAsyncService(TaskHistoryRepository taskHistoryRepository,
                            ProcessInstanceHistoryRepository processInstanceHistoryRepository,
                            ProcessInstanceAuditService processInstanceAuditService,
                            TaskHistoryAuditService taskHistoryAuditService) {
        this.taskHistoryRepository = taskHistoryRepository;
        this.processInstanceHistoryRepository = processInstanceHistoryRepository;
        this.processInstanceAuditService = processInstanceAuditService;
        this.taskHistoryAuditService = taskHistoryAuditService;
    }

    //TODO add logging and validations where needed
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveAllAsync(List<TaskHistory> taskHistoryList) {
        if(CollectionUtils.isEmpty(taskHistoryList)) {
            return;
        }
        logger.info("Successfully saved task history list");
        taskHistoryRepository.saveAll(taskHistoryList);
    }

    @Transactional
    public TaskHistory save(TaskHistory taskHistory) {
        if(taskHistory == null) {
            return null;
        }
        logger.info(String.format("Successfully saved task history with id %s", taskHistory.getId()));
        return taskHistoryRepository.save(taskHistory);
    }

    @Transactional
    public void saveTaskFromCamunda(CamundaHttpConnectorRequest request) {
        if (request == null || request.getVariables() == null) {
            logger.error("Payload or Variables map is NULL. Check if JSON structure matches DTO.");
            return;
        }
        logger.info("Started the process of saving task history from camunda");
        int retry = 0;
        while(retry < 15) {
            Optional<ProcessInstanceHistory> pih = processInstanceHistoryRepository.findById(request.getVariables().get("foundProcessInstanceId"));
            logger.info("Task create operation retry {}", retry);
            if(pih.isPresent()) {
                pih.get().setModifiedTime(null);
                pih.get().setOperationUser(null);
                TaskHistory savedTaskHistory = save(TaskHistoryMapper.toTaskHistoryFromConnectorVariables(request.getVariables()));
                taskHistoryRepository.save(savedTaskHistory);
                //call audit for process instance
                processInstanceAuditService.saveProcessInstanceAudit(pih.get(), Action.TASK_CREATE);
                //call audit for task
                taskHistoryAuditService.saveTaskHistoryAuditHistory(savedTaskHistory, Action.TASK_CREATE);
                logger.info(String.format("Successfully saved Task History with id %s", savedTaskHistory.getId()));
                return;
            }
            retry++;
            LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(2));
        }
        logger.error("Task History save failed for process instance with id: {}", request.getVariables().get(CamundaVariableNames.PROCESS_INSTANCE_ID));
    }
}
