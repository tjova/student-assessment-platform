package pmf.master.platforma.audit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pmf.master.platforma.audit.entity.VariableHistoryAudit;

public interface VariableHistoryAuditRepository extends JpaRepository<VariableHistoryAudit, Integer> {

}
