package intra.home.controller;

import intra.home.model.FileDto;
import intra.home.service.FileAccessService;
import intra.home.service.FileRemovalService;
import intra.home.service.FileUploadService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
public class FileController {

    private final FileUploadService fileUploadService;
    private final FileRemovalService fileRemovalService;
    private final FileAccessService fileAccessService;

    @GetMapping("/get/file/{fileId}")
    public void getFile(@PathVariable("fileId") Long fileId, HttpServletResponse response) throws IOException {
        FileSystemResource imgFile = fileAccessService.getFileResource(fileId);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(imgFile.getInputStream(), response.getOutputStream());
    }

    @GetMapping("/download/file/{fileId}")
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@PathVariable Long fileId) {
        System.out.println(fileId);
        Resource file = fileAccessService.loadFileAsResource(fileId);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @GetMapping("/info/file/{fileId}")
    @ResponseBody
    public FileDto getFileInfo(@PathVariable("fileId") Long fileId) {
        return fileAccessService.getFileInfo(fileId);
    }

    @GetMapping("/get/files/{fileType}")
    @ResponseBody
    public List<Long> getFiles(@PathVariable("fileType") String fileType) {
        return fileAccessService.getFiles(fileType);
    }

    @GetMapping("/get/files")
    @ResponseBody
    public List<Long> getFiles() {
        return fileAccessService.getAllFiles();
    }

    @PostMapping("/upload")
    public void uploadFiles(@RequestParam("files") List<MultipartFile> files) {
        files.forEach(fileUploadService::uploadFile);
    }

    @DeleteMapping("/delete/{fileId}")
    public void deleteFile(@PathVariable("fileId") Long fileId) {
        fileRemovalService.deleteFile(fileId);
    }

    @DeleteMapping("/delete/types/{fileType}")
    public void deleteFiles(@PathVariable("fileType") String fileType) {
        fileRemovalService.deleteAllFilesWithType(fileType);
    }

    @DeleteMapping("/delete")
    public void deleteAllFiles() {
        fileRemovalService.deleteAllFiles();
    }
}
