package pmf.master.platforma.main.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class VariableDto {
    private String name;
    private String type;
    private String value;
    @JsonIgnore
    private String processInstanceId;
    @JsonIgnore
    private String taskId;
    @JsonIgnore
    private LocalDateTime createdTime;
    @JsonIgnore
    private LocalDateTime modifiedTime;

    public VariableDto(String name, String type, String value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }
}
