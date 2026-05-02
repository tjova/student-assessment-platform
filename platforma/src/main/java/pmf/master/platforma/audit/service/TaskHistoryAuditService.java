package pmf.master.platforma.audit.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pmf.master.platforma.audit.entity.TaskHistoryAudit;
import pmf.master.platforma.audit.repository.TaskHistoryAuditRepository;
import pmf.master.platforma.exceptionHandling.BadRequestException;
import pmf.master.platforma.main.entity.TaskHistory;
import pmf.master.platforma.mapper.TaskHistoryMapper;
import pmf.master.platforma.util.Action;

//TODO - ADD LOGGING

@Service
public class TaskHistoryAuditService {

    private final Logger logger = LoggerFactory.getLogger(TaskHistoryAuditService.class);
    private final TaskHistoryAuditRepository taskHistoryAuditRepository;

    public TaskHistoryAuditService(TaskHistoryAuditRepository taskHistoryAuditRepository) {
        this.taskHistoryAuditRepository = taskHistoryAuditRepository;
    }

    public void saveTaskHistoryAuditHistory(TaskHistory taskHistory, Action action) {
        if(action == null) {
            throw new BadRequestException("Action must be declared for audit save");
        }

        TaskHistoryAudit taskHistoryAudit = TaskHistoryMapper.toTaskHistoryAudit(taskHistory);
        taskHistoryAudit.setAction(action);
        taskHistoryAuditRepository.save(taskHistoryAudit);
    }
}
