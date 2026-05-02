package pmf.master.platforma.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pmf.master.platforma.main.entity.TaskHistory;

import java.util.Optional;

@Repository
public interface TaskHistoryRepository extends JpaRepository<TaskHistory, String> {
    Optional<TaskHistory> findById(String taskId);

    @Query("SELECT th FROM TaskHistory th WHERE th.processInstanceHistoryId = :processInstanceHistoryId AND th.endTime IS NOT NULL ORDER BY th.endTime DESC LIMIT 1")
    Optional<TaskHistory> findFirstByProcessInstanceHistoryIdAndEndTimeNotNullOrderByEndTimeDesc(String processInstanceHistoryId);
}
