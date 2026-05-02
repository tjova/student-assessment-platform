package pmf.master.platforma.main.service;

import io.micrometer.common.util.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pmf.master.platforma.exceptionHandling.BadRequestException;
import pmf.master.platforma.main.entity.ProcessInstanceHistory;
import pmf.master.platforma.main.entity.VariableHistory;
import pmf.master.platforma.main.model.VariableDto;
import pmf.master.platforma.main.repository.ProcessInstanceHistoryRepository;
import pmf.master.platforma.main.repository.VariableHistoryRepository;
import pmf.master.platforma.mapper.VariableHistoryMapper;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VariableHistoryService {
    private final VariableHistoryRepository variableHistoryRepository;
    private final ProcessInstanceHistoryRepository processInstanceHistoryRepository;

    public VariableHistoryService(VariableHistoryRepository variableHistoryRepository,
                                  ProcessInstanceHistoryRepository processInstanceHistoryRepository) {
        this.variableHistoryRepository = variableHistoryRepository;
        this.processInstanceHistoryRepository = processInstanceHistoryRepository;
    }

    @Transactional(readOnly = true)
    public Map<String, List<VariableHistory>> getVariableMapForIdList(List<String> idList) {
        List<VariableHistory> variableHistoryList = variableHistoryRepository.findByProcessInstanceIdInOrderByProcessInstanceId(idList);
        return variableHistoryList.stream().collect(Collectors.groupingBy(VariableHistory::getProcessInstanceId));
    }

    @Transactional(readOnly = true)
    public List<VariableDto> getVariableForProcessInstance(String processInstanceId) {
        if(StringUtils.isBlank(processInstanceId)) {
            throw new BadRequestException("Process instance id must not be null");
        }

        Optional<ProcessInstanceHistory> processInstanceHistoryOptional = processInstanceHistoryRepository.findById(processInstanceId);
        if(processInstanceHistoryOptional.isEmpty()) {
            throw new BadRequestException(String.format("Process instance with id %s does not exist", processInstanceId));
        }

        return VariableHistoryMapper.toVariableDtoList(variableHistoryRepository.findByProcessInstanceIdOrderByNameAsc(processInstanceId));
    }
}
