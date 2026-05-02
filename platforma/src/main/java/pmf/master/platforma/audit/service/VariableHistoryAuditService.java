package pmf.master.platforma.audit.service;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import pmf.master.platforma.audit.repository.VariableHistoryAuditRepository;
import pmf.master.platforma.main.entity.VariableHistory;
import pmf.master.platforma.mapper.VariableHistoryMapper;
import pmf.master.platforma.util.Action;

import java.util.List;

@Service
public class VariableHistoryAuditService {

    private final VariableHistoryAuditRepository variableHistoryAuditRepository;

    public VariableHistoryAuditService(VariableHistoryAuditRepository variableHistoryAuditRepository) {
        this.variableHistoryAuditRepository = variableHistoryAuditRepository;
    }

    public void saveAuditVariables(List<VariableHistory> savedVariables, Action action, boolean modified) {
        if(CollectionUtils.isEmpty(savedVariables)) {
            return;
        }

        variableHistoryAuditRepository.saveAll(VariableHistoryMapper.toAuditList(savedVariables));
    }
}
