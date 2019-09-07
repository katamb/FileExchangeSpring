package intra.home.service;

import intra.home.enums.FileType;
import intra.home.exception.BadRequestException;
import intra.home.exception.FileStorageException;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Getter
@Service
public class PathProvidingFilenameService {

    private static final Logger logger = LoggerFactory.getLogger(PathProvidingFilenameService.class);
    private final String photosDir;
    private final Path photosDirLocation;
    private final String videosDir;
    private final Path videosDirLocation;
    private final String musicDir;
    private final Path musicDirLocation;
    private final String docsDir;
    private final Path docsDirLocation;

    public PathProvidingFilenameService(@Value("${file.photosDir}") String photosDir,
                                        @Value("${file.videosDir}") String videosDir,
                                        @Value("${file.musicDir}") String musicDir,
                                        @Value("${file.docsDir}") String docsDir) {
        this.photosDir = photosDir;
        this.videosDir = videosDir;
        this.musicDir = musicDir;
        this.docsDir = docsDir;

        this.photosDirLocation = Paths.get(photosDir).toAbsolutePath().normalize();
        this.videosDirLocation = Paths.get(videosDir).toAbsolutePath().normalize();
        this.musicDirLocation = Paths.get(musicDir).toAbsolutePath().normalize();
        this.docsDirLocation = Paths.get(docsDir).toAbsolutePath().normalize();

        this.createDirectoriesIfNotExisting();
    }

    private void createDirectoriesIfNotExisting() {
        try {
            Files.createDirectories(this.photosDirLocation);
            Files.createDirectories(this.videosDirLocation);
            Files.createDirectories(this.musicDirLocation);
            Files.createDirectories(this.docsDirLocation);
        } catch (Exception e) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", e);
        }
    }

    public Path getPathFromFileType(String fileType, String uniqueFileName) {
        if (fileType.equals(FileType.IMAGE.getLabel())) {
            return photosDirLocation.resolve(uniqueFileName);
        } else if (fileType.equals(FileType.AUDIO.getLabel())) {
            return musicDirLocation.resolve(uniqueFileName);
        } else if (fileType.equals(FileType.VIDEO.getLabel())) {
            return videosDirLocation.resolve(uniqueFileName);
        } else {
            return docsDirLocation.resolve(uniqueFileName);
        }
    }

    public String getFileExtension(String filename) {
        if (filename == null || filename.length() == 0) {
            logger.warn("Trying to upload nameless file!");
            throw new BadRequestException("Uploaded file needs to have a name!");
        }

        String[] fileNameString = filename.split("\\.");
        return fileNameString[fileNameString.length - 1]
                .trim()
                .toLowerCase();
    }

    public String getFileType(String fileType) {
        String[] fileNameString = fileType.split("/");
        return fileNameString[0]
                .trim()
                .toLowerCase();
    }
}
