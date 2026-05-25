package org.Profile.command.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfilePortfolioCommand {
    @TargetAggregateIdentifier
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
