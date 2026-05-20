package org.Profile.command.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.Profile.constant.Gender;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CreateProfileRequest {
    @NotBlank(message = "fullName không được để trống")
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
    private List<CreateEducationRequest> educations = new ArrayList<>();
    private List<CreateWorkExperienceRequest> experiences = new ArrayList<>();
    private List<CreateProfileSkillRequest> skills = new ArrayList<>();
    private List<CreateSocialLinkRequest> socialLinks = new ArrayList<>();
}
