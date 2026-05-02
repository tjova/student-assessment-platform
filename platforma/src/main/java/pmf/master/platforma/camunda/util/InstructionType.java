package pmf.master.platforma.camunda.util;

import lombok.Getter;

@Getter
public enum InstructionType {
    CANCEL("cancel"),
    START_BEFORE_ACTIVITY("startBeforeActivity"),
    START_AFTER_ACTIVITY("startAfterActivity"),
    START_TRANSITION("startTransition");

    private final String type;

    private InstructionType(String type) {
        this.type = type;
    }
}
