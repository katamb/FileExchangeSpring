package intra.home.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Photo {

    private Long photoId;
    private String fileName;
    private String originalFileName;
    private String fileExtension;

    public Photo(String fileName, String originalFileName, String fileExtension) {
        this.fileName = fileName;
        this.originalFileName = originalFileName;
        this.fileExtension = fileExtension;
    }

}
