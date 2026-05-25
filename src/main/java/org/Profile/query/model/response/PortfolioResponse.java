package org.Profile.query.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PortfolioResponse {
    private String id;
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
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
