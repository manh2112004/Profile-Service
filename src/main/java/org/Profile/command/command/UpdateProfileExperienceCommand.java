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
public class UpdateProfileExperienceCommand {
    @TargetAggregateIdentifier
    private String profileId;
    private String experienceId;
    private String companyName;
    private String position;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean currentlyWorking;
    private String description;
}
