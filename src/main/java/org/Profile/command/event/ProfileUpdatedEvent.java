package org.Profile.command.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.Profile.command.model.request.CreateEducationRequest;
import org.Profile.command.model.request.CreateProfileSkillRequest;
import org.Profile.command.model.request.CreateSocialLinkRequest;
import org.Profile.command.model.request.CreateWorkExperienceRequest;
import org.Profile.constant.Gender;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileUpdatedEvent {
    private String id;
    private String fullName;
    private String avatarUrl;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private Gender gender;
    private String address;
    private String city;
    private String country;
    private String headline;
    private String summary;
    private String currentPosition;
    private String currentCompany;
    private Integer yearsOfExperience;
    private String expectedJobTitle;
    private String expectedLocation;
    private Double expectedSalary;
    private List<CreateEducationRequest> educations;
    private List<CreateWorkExperienceRequest> experiences;
    private List<CreateProfileSkillRequest> skills;
    private List<CreateSocialLinkRequest> socialLinks;
}
