package pmf.master.platforma.audit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pmf.master.platforma.audit.entity.TaskHistoryAudit;
import pmf.master.platforma.main.entity.TaskHistory;

public interface TaskHistoryAuditRepository extends JpaRepository<TaskHistoryAudit, String> {
}
