package org.Profile.command.model.request;

import lombok.Getter;
import lombok.Setter;
import org.Profile.constant.SkillLevel;

@Getter
@Setter
public class CreateProfileSkillRequest {
    private String skillName;
    private SkillLevel level;
    private Integer yearsOfExperience;
}
