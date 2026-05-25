package org.Profile.command.model.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CreatePortfolioRequest {
    private String title;
    private String description;
    private String imageUrl;
    private String projectUrl;
    private String githubUrl;
    private String role;
    private String organization;
    private String technologies;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean currentlyWorking;
    private Boolean isPublic;
    private Integer displayOrder;
    private Boolean removeImage;
}
