package intra.home.controller;

import intra.home.enums.StatusType;
import intra.home.mapper.LinkMapper;
import intra.home.mapper.SubjectMapper;
import intra.home.mapper.TodoMapper;
import intra.home.model.LinkDto;
import intra.home.model.SubjectDto;
import intra.home.model.TodoDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class SchoolController {

    private final SubjectMapper subjectMapper;
    private final TodoMapper todoMapper;
    private final LinkMapper linkMapper;

    @GetMapping("/api/school/all/subjects")
    public List<SubjectDto> getAllSubjects() {
        return subjectMapper.getAllSubjects();
    }

    @PostMapping("/api/school/add/subject")
    public void addSubject(@RequestBody SubjectDto subject) {
        subjectMapper.addSubject(subject);
    }

    @GetMapping("/api/school/todo/{subjectId}")
    public List<TodoDto> getTodosBySubject(@PathVariable Long subjectId) {
        return todoMapper.getAllTodosBySubject(subjectId);
    }

    @PostMapping("/api/school/add/todo")
    public void addTodo(@RequestBody TodoDto subject) {
        subject.setTodoStatus(StatusType.TODO.getLabel());
        todoMapper.addTodo(subject);
    }

    @PutMapping("/api/school/update/todo")
    public void updateTodoStatus(@RequestBody TodoDto subject) {
        todoMapper.updateTodoStatus(subject);
    }

    @GetMapping("/api/school/link/{subjectId}")
    public List<LinkDto> getLinksBySubject(@PathVariable Long subjectId) {
        return linkMapper.getAllLinksBySubject(subjectId);
    }

    @PostMapping("/api/school/add/link")
    public void addLink(@RequestBody LinkDto subject) {
        System.out.println(subject.toString());
        linkMapper.addLink(subject);
    }
}
