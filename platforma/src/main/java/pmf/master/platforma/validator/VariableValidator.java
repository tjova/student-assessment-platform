package pmf.master.platforma.validator;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import pmf.master.platforma.exceptionHandling.BadRequestException;
import pmf.master.platforma.main.entity.VariableHistory;
import pmf.master.platforma.main.repository.VariableHistoryRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class VariableValidator {

    private final VariableHistoryRepository variableHistoryRepository;

    public void checkForDuplicateVarNames(List<VariableHistory> variableList) {
        if(CollectionUtils.isEmpty(variableList)) {
            return;
        }
        Set<String> variableNames = new HashSet<>();
        for(VariableHistory var : variableList) {
            if(!variableNames.add(var.getName())) {
                throw new BadRequestException(String.format("A variable with this name can not be sent more than once: %s", var.getName()));
            }
        }
    }
}
