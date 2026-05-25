package org.Profile.command.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioAddedToProfileEvent {
    private String profileId;
    private String portfolioId;
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
}
