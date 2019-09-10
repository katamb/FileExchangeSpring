package intra.home.mapper;

import intra.home.model.LinkDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LinkMapper {

    List<LinkDto> getAllLinksBySubject(Long subjectId);

    void addLink(LinkDto subject);

}
