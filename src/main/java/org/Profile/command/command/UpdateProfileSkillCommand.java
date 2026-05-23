package org.Profile.command.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.Profile.constant.SkillLevel;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfileSkillCommand {
    @TargetAggregateIdentifier
    private String profileId;
    private String skillId;
    private String skillName;
    private SkillLevel level;
    private Integer yearsOfExperience;
}
