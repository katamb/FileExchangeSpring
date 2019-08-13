package intra.home.controller;

import intra.home.model.Photo;
import intra.home.service.PhotoService;
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
public class PhotoController {

    private final PhotoService photoService;

    @GetMapping("/get/photo/{photoId}")
    public void getImage(@PathVariable("photoId") Long photoId, HttpServletResponse response) throws IOException {
        FileSystemResource imgFile = photoService.getPhotoResource(photoId);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(imgFile.getInputStream(), response.getOutputStream());
    }

    @GetMapping("/download/photo/{photoId}")
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@PathVariable Long photoId) {
        Resource file = photoService.loadFileAsResource(photoId);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @GetMapping("/info/photo/{photoId}")
    @ResponseBody
    public Photo getImageInfo(@PathVariable("photoId") Long photoId) {
        return photoService.getPhotoInfo(photoId);
    }

    @GetMapping("/get/photos")
    @ResponseBody
    public List<Long> getPhotos() {
        return photoService.getPhotos();
    }

    @PostMapping("/upload/photos")
    public void uploadPhotos(@RequestParam("pics") List<MultipartFile> photos) {
        for (MultipartFile photo : photos) {
            photoService.uploadPhoto(photo);
        }
    }

    @DeleteMapping("/delete/photo/{photoId}")
    public void uploadPhotos(@PathVariable("photoId") Long photoId) {
        photoService.deleteFile(photoId);
    }
}
