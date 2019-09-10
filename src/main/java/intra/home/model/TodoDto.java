package intra.home.model;

import lombok.Data;

@Data
public class TodoDto {

    private Long todoId;
    private Long todoSubject;
    private String todoContent;
    private String todoStatus;

}
