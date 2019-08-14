package intra.home.service;

import intra.home.conf.FileStorageProperties;
import intra.home.exception.FileStorageException;
import intra.home.mapper.PhotoMapper;
import intra.home.model.Photo;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Service
public class PhotoService {

    private final Path fileStorageLocation;
    private final FileStorageProperties fileStorageProperties;
    private final PhotoMapper photoMapper;
    private final Set<String> whitelistedFileExtensions;

    public PhotoService(FileStorageProperties fileStorageProperties,
                        PhotoMapper photoMapper) {
        // Full path to uploads directory
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();
        this.fileStorageProperties = fileStorageProperties;
        this.photoMapper = photoMapper;

        Set<String> whitelist = new HashSet<>(Arrays.asList("jpg", "png", "gif", "jpeg"));
        this.whitelistedFileExtensions = Collections.unmodifiableSet(whitelist);

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception e) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", e);
        }
    }

    public void uploadPhoto(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();
        String[] fileNameString = originalFileName.split("\\.");
        String extension = fileNameString[fileNameString.length - 1]
                .replaceAll("\\s+", "")
                .toLowerCase();
        String uniqueFileName = UUID.randomUUID().toString() + "." + extension;

        if (!this.whitelistedFileExtensions.contains(extension)) {
            String errorMessage = String.format("File type not supported! \n Supported file types: %s",
                    String.join(", ", whitelistedFileExtensions));
            throw new FileStorageException(errorMessage);
        }

        Photo photo = new Photo();
        photo.setOriginalFileName(StringUtils.cleanPath(originalFileName));
        photo.setFileName(uniqueFileName);
        photo.setFileExtension(extension);

        try {
            photoMapper.savePhotoData(photo);
            Path targetLocation = fileStorageLocation.resolve(uniqueFileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + originalFileName + ". Please try again!", ex);
        }
    }

    public FileSystemResource getPhotoResource(Long photoId) {
        String fileName = photoMapper.findFileNameById(photoId);
        return new FileSystemResource(fileStorageProperties.getUploadDir() + fileName);
    }

    public List<Long> getPhotos() {
        return photoMapper.getPhotoIds();
    }

    public Photo getPhotoInfo(Long photoId) {
        return photoMapper.findFileById(photoId);
    }

    public void deleteFile(Long fileId) {
        Photo photo = photoMapper.findFileById(fileId);
        photoMapper.deleteFileById(fileId);
        try {
            Path targetLocation = fileStorageLocation.resolve(photo.getFileName());
            Files.delete(targetLocation);
        } catch (IOException ex) {
            String errorMessage = String.format("Could not delete file %s. Please try again!",
                    photo.getOriginalFileName());
            throw new FileStorageException(errorMessage, ex);
        }
    }

    public Resource loadFileAsResource(Long fileId) {
        String fileName = photoMapper.findFileById(fileId).getFileName();
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new FileStorageException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new FileStorageException("File not found " + fileName, ex);
        }
    }
}
