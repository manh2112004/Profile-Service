package org.Profile.command.data;

import jakarta.persistence.*;
import lombok.*;
import org.Profile.constant.SkillLevel;

@Entity
@Table(name = "profile_skills")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileSkill {
    @Id
    private String id;

    @Column(nullable = false)
    private String skillName;

    @Enumerated(EnumType.STRING)
    private SkillLevel level;

    private Integer yearsOfExperience;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    private Profile profile;
}
