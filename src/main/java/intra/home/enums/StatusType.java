package intra.home.enums;

import lombok.Getter;

@Getter
public enum StatusType {

    TODO("To Do"),
    IN_PROGRESS("In Progress"),
    IN_REVIEW("In Review"),
    DONE("Done");

    private String label;

    private StatusType(String label) {
        this.label = label;
    }
}
