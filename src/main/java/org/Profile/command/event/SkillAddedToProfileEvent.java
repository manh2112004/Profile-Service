package org.Profile.command.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.Profile.constant.SkillLevel;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SkillAddedToProfileEvent {
    private String profileId;
    private String skillId;
    private String skillName;
    private SkillLevel level;
    private Integer yearsOfExperience;
}
