package pmf.master.platforma.audit.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pmf.master.platforma.audit.entity.ProcessInstanceAudit;
import pmf.master.platforma.audit.repository.ProcessInstanceAuditRepository;
import pmf.master.platforma.exceptionHandling.BadRequestException;
import pmf.master.platforma.main.entity.ProcessInstanceHistory;
import pmf.master.platforma.mapper.ProcessInstanceHistoryMapper;
import pmf.master.platforma.util.Action;

import java.time.LocalDateTime;

@Service
public class ProcessInstanceAuditService {

    private final Logger logger = LoggerFactory.getLogger(ProcessInstanceAuditService.class);
    private final ProcessInstanceAuditRepository processInstanceAuditRepository;

    public ProcessInstanceAuditService(ProcessInstanceAuditRepository processInstanceAuditRepository) {
        this.processInstanceAuditRepository = processInstanceAuditRepository;
    }

    @Transactional
    public void saveProcessInstanceAudit(ProcessInstanceHistory savedProcessInstance, Action action) {
        if(savedProcessInstance == null) {
            throw new BadRequestException("No process instance for save");
        }

        saveProcessInstanceHistoryAudit(savedProcessInstance, action);
        logger.info(String.format("Successfully saved process instance with id %s", savedProcessInstance.getProcessInstanceHistoryId()));
    }

    private void saveProcessInstanceHistoryAudit(ProcessInstanceHistory savedProcessInstance, Action action) {
        ProcessInstanceAudit processInstanceAudit = ProcessInstanceHistoryMapper.toProcessInstanceAudit(savedProcessInstance);
        processInstanceAudit.setAction(action);
        processInstanceAudit.setModifiedTime(LocalDateTime.now());
        processInstanceAuditRepository.save(processInstanceAudit);
    }
}
