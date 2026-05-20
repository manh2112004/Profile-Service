package org.Profile.query.queries;

import org.Profile.command.data.Education;
import org.Profile.command.data.Profile;
import org.Profile.command.data.ProfileRepository;
import org.Profile.command.data.ProfileSkill;
import org.Profile.command.data.SocialLink;
import org.Profile.command.data.WorkExperience;
import org.Profile.query.model.response.EducationResponse;
import org.Profile.query.model.response.ProfileResponse;
import org.Profile.query.model.response.ProfileSkillResponse;
import org.Profile.query.model.response.SocialLinkResponse;
import org.Profile.query.model.response.WorkExperienceResponse;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;

@Component
public class ProfileQueryHandler {
    @Autowired
    private ProfileRepository profileRepository;

    @QueryHandler
    @Transactional(readOnly = true)
    public ProfileResponse handle(GetMyProfileQuery query) {
        Profile profile = profileRepository.findByUserId(query.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile không tồn tại"));
        return mapToResponse(profile);
    }

    @QueryHandler
    @Transactional(readOnly = true)
    public ProfileResponse handle(GetProfileByIdQuery query) {
        Profile profile = profileRepository.findById(query.getProfileId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile không tồn tại"));
        return mapToResponse(profile);
    }

    @QueryHandler
    @Transactional(readOnly = true)
    public List<EducationResponse> handle(GetProfileEducationsQuery query) {
        Profile profile = profileRepository.findById(query.getProfileId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile không tồn tại"));
        return mapEducations(profile);
    }

    private ProfileResponse mapToResponse(Profile profile) {
        return ProfileResponse.builder()
                .id(profile.getId())
                .userId(profile.getUserId())
                .fullName(profile.getFullName())
                .avatarUrl(profile.getAvatarUrl())
                .phoneNumber(profile.getPhoneNumber())
                .dateOfBirth(profile.getDateOfBirth())
                .gender(profile.getGender())
                .address(profile.getAddress())
                .city(profile.getCity())
                .country(profile.getCountry())
                .headline(profile.getHeadline())
                .summary(profile.getSummary())
                .currentPosition(profile.getCurrentPosition())
                .currentCompany(profile.getCurrentCompany())
                .yearsOfExperience(profile.getYearsOfExperience())
                .expectedJobTitle(profile.getExpectedJobTitle())
                .expectedLocation(profile.getExpectedLocation())
                .expectedSalary(profile.getExpectedSalary())
                .status(profile.getStatus())
                .educations(mapEducations(profile))
                .experiences(mapExperiences(profile))
                .skills(mapSkills(profile))
                .socialLinks(mapSocialLinks(profile))
                .createdAt(profile.getCreatedAt())
                .updatedAt(profile.getUpdatedAt())
                .build();
    }

    private List<EducationResponse> mapEducations(Profile profile) {
        return profile.getEducations().stream()
                .sorted(Comparator.comparing(Education::getStartDate, Comparator.nullsLast(Comparator.reverseOrder())))
                .map(education -> EducationResponse.builder()
                        .id(education.getId())
                        .schoolName(education.getSchoolName())
                        .degree(education.getDegree())
                        .fieldOfStudy(education.getFieldOfStudy())
                        .startDate(education.getStartDate())
                        .endDate(education.getEndDate())
                        .currentlyStudying(education.getCurrentlyStudying())
                        .description(education.getDescription())
                        .build())
                .toList();
    }

    private List<WorkExperienceResponse> mapExperiences(Profile profile) {
        return profile.getExperiences().stream()
                .sorted(Comparator.comparing(WorkExperience::getStartDate, Comparator.nullsLast(Comparator.reverseOrder())))
                .map(experience -> WorkExperienceResponse.builder()
                        .id(experience.getId())
                        .companyName(experience.getCompanyName())
                        .position(experience.getPosition())
                        .startDate(experience.getStartDate())
                        .endDate(experience.getEndDate())
                        .currentlyWorking(experience.getCurrentlyWorking())
                        .description(experience.getDescription())
                        .build())
                .toList();
    }

    private List<ProfileSkillResponse> mapSkills(Profile profile) {
        return profile.getSkills().stream()
                .sorted(Comparator.comparing(ProfileSkill::getSkillName, Comparator.nullsLast(String::compareToIgnoreCase)))
                .map(skill -> ProfileSkillResponse.builder()
                        .id(skill.getId())
                        .skillName(skill.getSkillName())
                        .level(skill.getLevel())
                        .yearsOfExperience(skill.getYearsOfExperience())
                        .build())
                .toList();
    }

    private List<SocialLinkResponse> mapSocialLinks(Profile profile) {
        return profile.getSocialLinks().stream()
                .sorted(Comparator.comparing(link -> link.getPlatform() == null ? "" : link.getPlatform().name()))
                .map(link -> SocialLinkResponse.builder()
                        .id(link.getId())
                        .platform(link.getPlatform())
                        .url(link.getUrl())
                        .build())
                .toList();
    }
}
