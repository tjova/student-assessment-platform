package pmf.master.platforma.main.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "variable")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class VariableHistory {
    @Id
    private String id;
    private String name;
    private String type;
    private String value;
    private String processInstanceId;
    private String taskId;
    private LocalDateTime createdTime;
    private LocalDateTime modifiedTime;
}
