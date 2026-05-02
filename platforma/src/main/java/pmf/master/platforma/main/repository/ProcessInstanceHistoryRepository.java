package pmf.master.platforma.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pmf.master.platforma.main.entity.ProcessInstanceHistory;

@Repository
public interface ProcessInstanceHistoryRepository extends JpaRepository<ProcessInstanceHistory, String> {

}
