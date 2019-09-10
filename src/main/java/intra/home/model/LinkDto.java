package intra.home.model;

import lombok.Data;

@Data
public class LinkDto {

    private Long linkId;
    private Long linkSubject;
    private String linkContent;
    private String linkUrl;

}
