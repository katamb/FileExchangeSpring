package intra.home.service;

import intra.home.exception.FileStorageException;
import intra.home.mapper.FileMapper;
import intra.home.model.FileDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
public class FileRemovalService {

    private final FileMapper fileMapper;
    private final PathProvidingFilenameService pathProvidingFilenameService;

    public FileRemovalService(FileMapper fileMapper, PathProvidingFilenameService pathProvidingFilenameService) {
        this.fileMapper = fileMapper;
        this.pathProvidingFilenameService = pathProvidingFilenameService;
    }

    @Transactional
    public void deleteFile(Long fileId) {
        FileDto file = fileMapper.findFileById(fileId);
        fileMapper.deleteFileById(fileId);
        deleteFromFileStorage(file);
    }

    @Transactional
    public void deleteAllFilesWithType(String fileType) {
        List<FileDto> files = fileMapper.findFilesByType(fileType);
        fileMapper.deleteFilesByType(fileType);

        for (FileDto file : files) {
            deleteFromFileStorage(file);
        }
    }

    private void deleteFromFileStorage(FileDto file) {
        try {
            Path targetLocation =
                    pathProvidingFilenameService.getPathFromFileType(file.getFileType(), file.getUniqueFileName());
            Files.delete(targetLocation);
        } catch (IOException ex) {
            String errorMessage = String.format("Could not delete file %s. Please try again!",
                    file.getOriginalFileName());
            throw new FileStorageException(errorMessage, ex);
        }
    }

    @Transactional
    public void deleteAllFiles() {
        List<FileDto> files = fileMapper.findAllFiles();
        fileMapper.deleteAllFiles();

        for (FileDto file : files) {
            deleteFromFileStorage(file);
        }
    }
}
