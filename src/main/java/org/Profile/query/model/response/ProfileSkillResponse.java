package org.Profile.query.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.Profile.constant.SkillLevel;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileSkillResponse {
    private String id;
    private String skillName;
    private SkillLevel level;
    private Integer yearsOfExperience;
}
