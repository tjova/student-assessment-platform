package pmf.master.platforma.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pmf.master.platforma.main.entity.VariableHistory;

import java.util.List;

public interface VariableHistoryRepository extends JpaRepository<VariableHistory, String> {
    List<VariableHistory> findByProcessInstanceIdInOrderByProcessInstanceId(List<String> parentIdList);
    List<VariableHistory> findByProcessInstanceIdOrderByNameAsc(String processInstanceId);
}
