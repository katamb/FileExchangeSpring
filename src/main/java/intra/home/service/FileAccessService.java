package intra.home.service;

import intra.home.exception.BadRequestException;
import intra.home.exception.FileStorageException;
import intra.home.mapper.FileMapper;
import intra.home.model.FileDto;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.List;

@Service
public class FileAccessService {

    private final FileMapper fileMapper;
    private final PathProvidingFilenameService pathProvidingFilenameService;

    public FileAccessService(FileMapper fileMapper, PathProvidingFilenameService pathProvidingFilenameService) {
        this.fileMapper = fileMapper;
        this.pathProvidingFilenameService = pathProvidingFilenameService;
    }

    public List<Long> getFiles(String fileType) {
        return fileMapper.getFileIds(fileType);
    }

    public List<Long> getAllFiles() {
        return fileMapper.getAllFileIds();
    }

    public FileDto getFileInfo(Long fileId) {
        FileDto file = fileMapper.findFileById(fileId);
        if (file != null) {
            return file;
        } else {
            throw new BadRequestException("File with this ID does not exist!");
        }
    }

    public FileSystemResource getFileResource(Long fileId) {
        FileDto file = fileMapper.findFileById(fileId);
        return new FileSystemResource(pathProvidingFilenameService.getPathFromFileType(file.getFileType(),
                file.getUniqueFileName()));
    }

    public Resource loadFileAsResource(Long fileId) {
        FileDto file = fileMapper.findFileById(fileId);
        try {
            Path filePath = pathProvidingFilenameService
                    .getPathFromFileType(file.getFileType(), file.getUniqueFileName())
                    .normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                return resource;
            } else {
                throw new FileStorageException("FileDto not found " + file.getOriginalFileName());
            }
        } catch (MalformedURLException ex) {
            throw new FileStorageException("FileDto not found " + file.getOriginalFileName(), ex);
        }
    }

}
