package org.Profile.command.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.Profile.command.model.request.CreateEducationRequest;
import org.Profile.command.model.request.CreateProfileSkillRequest;
import org.Profile.command.model.request.CreateSocialLinkRequest;
import org.Profile.command.model.request.CreateWorkExperienceRequest;
import org.Profile.constant.Gender;
import org.Profile.constant.ProfileStatus;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class CreateProfileCommand {
    @TargetAggregateIdentifier
    private final String id;
    private final String userId;
    private final String fullName;
    private final String avatarUrl;
    private final String phoneNumber;
    private final LocalDate dateOfBirth;
    private final Gender gender;
    private final String address;
    private final String city;
    private final String country;
    private final String headline;
    private final String summary;
    private final String currentPosition;
    private final String currentCompany;
    private final Integer yearsOfExperience;
    private final String expectedJobTitle;
    private final String expectedLocation;
    private final Double expectedSalary;
    private final ProfileStatus status;
    private final List<CreateEducationRequest> educations;
    private final List<CreateWorkExperienceRequest> experiences;
    private final List<CreateProfileSkillRequest> skills;
    private final List<CreateSocialLinkRequest> socialLinks;
}
