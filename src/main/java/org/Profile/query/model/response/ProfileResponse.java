package org.Profile.query.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.Profile.constant.Gender;
import org.Profile.constant.ProfileStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileResponse {
    private String id;
    private String userId;
    private String fullName;
    private String avatarUrl;
    private String coverImageUrl;
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
    private ProfileStatus status;
    @Builder.Default
    private List<EducationResponse> educations = new ArrayList<>();
    @Builder.Default
    private List<WorkExperienceResponse> experiences = new ArrayList<>();
    @Builder.Default
    private List<ProfileSkillResponse> skills = new ArrayList<>();
    @Builder.Default
    private List<SocialLinkResponse> socialLinks = new ArrayList<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
