package intra.home.mapper;

import intra.home.model.FileDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FileMapper {

    FileDto findFileById(Long fileId);

    List<FileDto> findFilesByType(String fileType);

    List<FileDto> findAllFiles();

    List<Long> getFileIds(String fileType);

    List<Long> getAllFileIds();

    void saveFileData(FileDto file);

    void deleteFileById(Long fileId);

    void deleteFilesByType(String fileType);

    void deleteAllFiles();

}
