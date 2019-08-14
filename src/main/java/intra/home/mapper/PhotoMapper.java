package intra.home.mapper;

import intra.home.model.Photo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PhotoMapper {

    @Insert("INSERT INTO photo (fileName, originalFileName, fileExtension) VALUES (#{fileName}, #{originalFileName}, #{fileExtension})")
    void savePhotoData(Photo photo);

    @Delete("DELETE FROM photo WHERE photoId = #{photoId}")
    void deleteFileById(Long photoId);

    @Select("SELECT fileName FROM photo WHERE photoId = #{photoId}")
    String findFileNameById(Long photoId);

    @Select("SELECT * FROM photo WHERE photoId = #{photoId}")
    Photo findFileById(Long photoId);

    @Select("SELECT photoId FROM photo")
    List<Long> getPhotoIds();

}
