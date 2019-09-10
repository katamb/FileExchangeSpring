package intra.home.mapper;

import intra.home.model.SubjectDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SubjectMapper {

    List<SubjectDto> getAllSubjects();

    void addSubject(SubjectDto subjectName);

}
