package pmf.master.platforma.audit.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "variable_history_audit")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class VariableHistoryAudit {
    @Id
    private Integer id;
    private String variableHistoryId;
    private String name;
    private String type;
    private String value;
    private String processInstanceId;
    private String taskId;
    private LocalDateTime createdTime;
    private LocalDateTime modifiedTime;
}
