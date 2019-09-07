package intra.home.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class FileDto {

    private Long fileId;
    private String originalFileName;
    private String fileExtension;
    private String uniqueFileName;
    private Long fileSize;
    private String fileType;
    private Date createdAt;

    public FileDto(String uniqueFileName, String originalFileName, String fileExtension) {
        this.uniqueFileName = uniqueFileName;
        this.originalFileName = originalFileName;
        this.fileExtension = fileExtension;
    }

}
