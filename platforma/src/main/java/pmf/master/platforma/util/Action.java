package pmf.master.platforma.util;

import lombok.Getter;


@Getter
public enum Action {
    INSTANCE_CREATE("INSTANCE_CREATE"),
    INSTANCE_UPDATE("INSTANCE_UPDATE"),
    VARIABLE_DELETE("VARIABLE_DELETE"),
    TASK_CREATE("TASK_CREATE"),
    ASSIGN("ASSIGN"),
    CLAIM("CLAIM"),
    UNCLAIM("UNCLAIM"),
    TASK_UPDATE("TASK_UPDATE"),
    TASK_DELETE("TASK_DELETE"),
    TASK_COMPLETE("TASK_COMPLETE"),
    INSTANCE_COMPLETE("INSTANCE_COMPLETE");

    private final String action;

    Action(String action) {
        this.action = action;
    }
}
