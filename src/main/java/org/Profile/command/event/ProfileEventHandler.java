package org.Profile.command.event;

import org.Profile.command.data.Education;
import org.Profile.command.data.Profile;
import org.Profile.command.data.ProfileRepository;
import org.Profile.command.data.ProfileSkill;
import org.Profile.command.data.SocialLink;
import org.Profile.command.data.WorkExperience;
import org.Profile.command.model.request.CreateEducationRequest;
import org.Profile.command.model.request.CreateProfileSkillRequest;
import org.Profile.command.model.request.CreateSocialLinkRequest;
import org.Profile.command.model.request.CreateWorkExperienceRequest;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.HashSet;
import java.util.UUID;

@Component
public class ProfileEventHandler {
    @Autowired
    private ProfileRepository profileRepository;

    @EventHandler
    public void on(ProfileCreatedEvent event) {
        Profile profile = Profile.builder()
                .id(event.getId())
                .userId(event.getUserId())
                .fullName(event.getFullName())
                .avatarUrl(event.getAvatarUrl())
                .phoneNumber(event.getPhoneNumber())
                .dateOfBirth(event.getDateOfBirth())
                .gender(event.getGender())
                .address(event.getAddress())
                .city(event.getCity())
                .country(event.getCountry())
                .headline(event.getHeadline())
                .summary(event.getSummary())
                .currentPosition(event.getCurrentPosition())
                .currentCompany(event.getCurrentCompany())
                .yearsOfExperience(event.getYearsOfExperience())
                .expectedJobTitle(event.getExpectedJobTitle())
                .expectedLocation(event.getExpectedLocation())
                .expectedSalary(event.getExpectedSalary())
                .status(event.getStatus())
                .educations(new HashSet<>())
                .experiences(new HashSet<>())
                .skills(new HashSet<>())
                .socialLinks(new HashSet<>())
                .build();

        if (event.getEducations() != null) {
            for (CreateEducationRequest request : event.getEducations()) {
                Education education = Education.builder()
                        .id(UUID.randomUUID().toString())
                        .schoolName(request.getSchoolName())
                        .degree(request.getDegree())
                        .fieldOfStudy(request.getFieldOfStudy())
                        .startDate(request.getStartDate())
                        .endDate(request.getEndDate())
                        .currentlyStudying(request.getCurrentlyStudying())
                        .description(request.getDescription())
                        .profile(profile)
                        .build();
                profile.getEducations().add(education);
            }
        }

        if (event.getExperiences() != null) {
            for (CreateWorkExperienceRequest request : event.getExperiences()) {
                WorkExperience experience = WorkExperience.builder()
                        .id(UUID.randomUUID().toString())
                        .companyName(request.getCompanyName())
                        .position(request.getPosition())
                        .startDate(request.getStartDate())
                        .endDate(request.getEndDate())
                        .currentlyWorking(request.getCurrentlyWorking())
                        .description(request.getDescription())
                        .profile(profile)
                        .build();
                profile.getExperiences().add(experience);
            }
        }

        if (event.getSkills() != null) {
            for (CreateProfileSkillRequest request : event.getSkills()) {
                ProfileSkill skill = ProfileSkill.builder()
                        .id(UUID.randomUUID().toString())
                        .skillName(request.getSkillName())
                        .level(request.getLevel())
                        .yearsOfExperience(request.getYearsOfExperience())
                        .profile(profile)
                        .build();
                profile.getSkills().add(skill);
            }
        }

        if (event.getSocialLinks() != null) {
            for (CreateSocialLinkRequest request : event.getSocialLinks()) {
                SocialLink socialLink = SocialLink.builder()
                        .id(UUID.randomUUID().toString())
                        .platform(request.getPlatform())
                        .url(request.getUrl())
                        .profile(profile)
                        .build();
                profile.getSocialLinks().add(socialLink);
            }
        }

        profileRepository.save(profile);
    }

    @EventHandler
    @Transactional
    public void on(ProfileUpdatedEvent event) {
        Profile profile = profileRepository.findById(event.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile không tồn tại"));

        if (event.getEducations() != null) {
            profile.getEducations().clear();
            for (CreateEducationRequest request : event.getEducations()) {
                Education education = Education.builder()
                        .id(UUID.randomUUID().toString())
                        .schoolName(request.getSchoolName())
                        .degree(request.getDegree())
                        .fieldOfStudy(request.getFieldOfStudy())
                        .startDate(request.getStartDate())
                        .endDate(request.getEndDate())
                        .currentlyStudying(request.getCurrentlyStudying())
                        .description(request.getDescription())
                        .profile(profile)
                        .build();
                profile.getEducations().add(education);
            }
        }

        if (event.getExperiences() != null) {
            profile.getExperiences().clear();
            for (CreateWorkExperienceRequest request : event.getExperiences()) {
                WorkExperience experience = WorkExperience.builder()
                        .id(UUID.randomUUID().toString())
                        .companyName(request.getCompanyName())
                        .position(request.getPosition())
                        .startDate(request.getStartDate())
                        .endDate(request.getEndDate())
                        .currentlyWorking(request.getCurrentlyWorking())
                        .description(request.getDescription())
                        .profile(profile)
                        .build();
                profile.getExperiences().add(experience);
            }
        }

        if (event.getSkills() != null) {
            profile.getSkills().clear();
            for (CreateProfileSkillRequest request : event.getSkills()) {
                ProfileSkill skill = ProfileSkill.builder()
                        .id(UUID.randomUUID().toString())
                        .skillName(request.getSkillName())
                        .level(request.getLevel())
                        .yearsOfExperience(request.getYearsOfExperience())
                        .profile(profile)
                        .build();
                profile.getSkills().add(skill);
            }
        }

        if (event.getSocialLinks() != null) {
            profile.getSocialLinks().clear();
            for (CreateSocialLinkRequest request : event.getSocialLinks()) {
                SocialLink socialLink = SocialLink.builder()
                        .id(UUID.randomUUID().toString())
                        .platform(request.getPlatform())
                        .url(request.getUrl())
                        .profile(profile)
                        .build();
                profile.getSocialLinks().add(socialLink);
            }
        }

        if (event.getFullName() != null) {
            profile.setFullName(event.getFullName());
        }
        if (event.getAvatarUrl() != null) {
            profile.setAvatarUrl(event.getAvatarUrl());
        }
        if (event.getPhoneNumber() != null) {
            profile.setPhoneNumber(event.getPhoneNumber());
        }
        if (event.getDateOfBirth() != null) {
            profile.setDateOfBirth(event.getDateOfBirth());
        }
        if (event.getGender() != null) {
            profile.setGender(event.getGender());
        }
        if (event.getAddress() != null) {
            profile.setAddress(event.getAddress());
        }
        if (event.getCity() != null) {
            profile.setCity(event.getCity());
        }
        if (event.getCountry() != null) {
            profile.setCountry(event.getCountry());
        }
        if (event.getHeadline() != null) {
            profile.setHeadline(event.getHeadline());
        }
        if (event.getSummary() != null) {
            profile.setSummary(event.getSummary());
        }
        if (event.getCurrentPosition() != null) {
            profile.setCurrentPosition(event.getCurrentPosition());
        }
        if (event.getCurrentCompany() != null) {
            profile.setCurrentCompany(event.getCurrentCompany());
        }
        if (event.getYearsOfExperience() != null) {
            profile.setYearsOfExperience(event.getYearsOfExperience());
        }
        if (event.getExpectedJobTitle() != null) {
            profile.setExpectedJobTitle(event.getExpectedJobTitle());
        }
        if (event.getExpectedLocation() != null) {
            profile.setExpectedLocation(event.getExpectedLocation());
        }
        if (event.getExpectedSalary() != null) {
            profile.setExpectedSalary(event.getExpectedSalary());
        }

        profileRepository.save(profile);
    }

    @EventHandler
    @Transactional
    public void on(EducationAddedToProfileEvent event) {
        Profile profile = profileRepository.findById(event.getProfileId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile không tồn tại"));

        Education education = Education.builder()
                .id(event.getEducationId())
                .schoolName(event.getSchoolName())
                .degree(event.getDegree())
                .fieldOfStudy(event.getFieldOfStudy())
                .startDate(event.getStartDate())
                .endDate(event.getEndDate())
                .currentlyStudying(event.getCurrentlyStudying())
                .description(event.getDescription())
                .profile(profile)
                .build();

        profile.getEducations().add(education);
        profileRepository.save(profile);
    }
}
