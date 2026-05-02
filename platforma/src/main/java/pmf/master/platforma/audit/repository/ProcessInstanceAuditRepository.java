package pmf.master.platforma.audit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pmf.master.platforma.audit.entity.ProcessInstanceAudit;

public interface ProcessInstanceAuditRepository extends JpaRepository<ProcessInstanceAudit, Integer> {
}
