package intra.home.service;

import intra.home.enums.FileType;
import intra.home.exception.FileStorageException;
import intra.home.mapper.FileMapper;
import intra.home.model.FileDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Service
public class FileUploadService {

    private final FileMapper fileMapper;
    private final PathProvidingFilenameService pathProvidingFilenameService;
    private final Set<String> blacklistedFileExtensions;

    public FileUploadService(FileMapper fileMapper, PathProvidingFilenameService pathProvidingFilenameService) {
        this.fileMapper = fileMapper;
        this.pathProvidingFilenameService = pathProvidingFilenameService;
        // Todo: think through, if and how strict this should be
        Set<String> blackList = new HashSet<>(Arrays.asList("run", "out", "ksh", "sh", "csh", "bin", "py", "java", "c",
                "cpp"));
        this.blacklistedFileExtensions = Collections.unmodifiableSet(blackList);
    }

    // Should do a rollback if saving to filestorage fails, needs testing
    @Transactional
    public void uploadFile(MultipartFile inputFile) {
        String originalFileName = inputFile.getOriginalFilename();
        String extension = pathProvidingFilenameService.getFileExtension(originalFileName);
        String fileType = pathProvidingFilenameService.getFileType(inputFile.getContentType());
        String uniqueFileName = UUID.randomUUID().toString() + "." + extension;

        checkIfAllowedFile(extension);

        FileDto file = new FileDto();
        file.setOriginalFileName(StringUtils.cleanPath(originalFileName));
        file.setUniqueFileName(uniqueFileName);
        file.setFileExtension(extension);
        file.setFileSize(inputFile.getSize());
        file.setFileType(fileType);
        Path targetLocation =
                pathProvidingFilenameService.getPathFromFileType(fileType, uniqueFileName);

        saveFileToDb(file);
        saveFileToFileStorage(inputFile, targetLocation, originalFileName);
    }

    private void checkIfAllowedFile(String fileExtension) {
        if (this.blacklistedFileExtensions.contains(fileExtension)) {
            String errorMessage = String.format("File type not supported! \n Unsupported file types: %s",
                    String.join(", ", blacklistedFileExtensions));
            throw new FileStorageException(errorMessage);
        }
    }

    private void saveFileToDb(FileDto file) {
        fileMapper.saveFileData(file);
    }

    private void saveFileToFileStorage(MultipartFile inputFile, Path targetLocation, String originalFileName) {
        try {
            Files.copy(inputFile.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new FileStorageException("Could not store file " + originalFileName + ". Please try again!", e);
        }
    }
}
