package intra.home.mapper;

import intra.home.model.TodoDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TodoMapper {

    List<TodoDto> getAllTodosBySubject(Long subjectId);

    void addTodo(TodoDto subject);

    void updateTodoStatus(TodoDto subject);
}
