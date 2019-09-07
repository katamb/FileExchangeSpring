package intra.home.enums;

import lombok.Getter;

@Getter
public enum FileType {

    IMAGE("image"),
    AUDIO("audio"),
    VIDEO("video"),
    APPLICATION("application");

    private String label;

    private FileType(String label) {
        this.label = label;
    }
}
