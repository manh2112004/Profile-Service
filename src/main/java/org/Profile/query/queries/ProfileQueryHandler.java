package org.Profile.query.queries;

import org.Profile.command.data.Education;
import org.Profile.command.data.Profile;
import org.Profile.command.data.ProfileRepository;
import org.Profile.command.data.ProfileSkill;
import org.Profile.command.data.SocialLink;
import org.Profile.command.data.WorkExperience;
import org.Profile.constant.ProfileStatus;
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
    public List<ProfileResponse> handle(SearchProfilesQuery query) {
        return profileRepository.searchProfiles(
                        query.getKeyword(),
                        query.getCity(),
                        query.getCountry(),
                        query.getSkill(),
                        ProfileStatus.ACTIVE
                ).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @QueryHandler
    @Transactional(readOnly = true)
    public List<EducationResponse> handle(GetProfileEducationsQuery query) {
        Profile profile = profileRepository.findById(query.getProfileId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile không tồn tại"));
        return mapEducations(profile);
    }

    @QueryHandler
    @Transactional(readOnly = true)
    public List<WorkExperienceResponse> handle(GetProfileExperiencesQuery query) {
        Profile profile = profileRepository.findById(query.getProfileId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile không tồn tại"));
        return mapExperiences(profile);
    }

    @QueryHandler
    @Transactional(readOnly = true)
    public List<ProfileSkillResponse> handle(GetProfileSkillsQuery query) {
        Profile profile = profileRepository.findById(query.getProfileId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile không tồn tại"));
        return mapSkills(profile);
    }

    @QueryHandler
    @Transactional(readOnly = true)
    public List<SocialLinkResponse> handle(GetProfileSocialLinksQuery query) {
        Profile profile = profileRepository.findById(query.getProfileId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile không tồn tại"));
        return mapSocialLinks(profile);
    }

    @QueryHandler
    @Transactional(readOnly = true)
    public WorkExperienceResponse handle(GetProfileExperienceDetailQuery query) {
        Profile profile = profileRepository.findById(query.getProfileId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile không tồn tại"));

        WorkExperience experience = profile.getExperiences().stream()
                .filter(existingExperience -> existingExperience.getId().equals(query.getExperienceId()))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Kinh nghiệm làm việc không tồn tại"));

        return mapExperience(experience);
    }

    @QueryHandler
    @Transactional(readOnly = true)
    public EducationResponse handle(GetProfileEducationDetailQuery query) {
        Profile profile = profileRepository.findById(query.getProfileId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile không tồn tại"));

        Education education = profile.getEducations().stream()
                .filter(existingEducation -> existingEducation.getId().equals(query.getEducationId()))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Học vấn không tồn tại"));

        return mapEducation(education);
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
                .map(this::mapEducation)
                .toList();
    }

    private EducationResponse mapEducation(Education education) {
        return EducationResponse.builder()
                .id(education.getId())
                .schoolName(education.getSchoolName())
                .degree(education.getDegree())
                .fieldOfStudy(education.getFieldOfStudy())
                .startDate(education.getStartDate())
                .endDate(education.getEndDate())
                .currentlyStudying(education.getCurrentlyStudying())
                .description(education.getDescription())
                .build();
    }

    private List<WorkExperienceResponse> mapExperiences(Profile profile) {
        return profile.getExperiences().stream()
                .sorted(Comparator.comparing(WorkExperience::getStartDate, Comparator.nullsLast(Comparator.reverseOrder())))
                .map(this::mapExperience)
                .toList();
    }

    private WorkExperienceResponse mapExperience(WorkExperience experience) {
        return WorkExperienceResponse.builder()
                .id(experience.getId())
                .companyName(experience.getCompanyName())
                .position(experience.getPosition())
                .startDate(experience.getStartDate())
                .endDate(experience.getEndDate())
                .currentlyWorking(experience.getCurrentlyWorking())
                .description(experience.getDescription())
                .build();
    }

    private List<ProfileSkillResponse> mapSkills(Profile profile) {
        return profile.getSkills().stream()
                .sorted(Comparator.comparing(ProfileSkill::getSkillName, Comparator.nullsLast(String::compareToIgnoreCase)))
                .map(this::mapSkill)
                .toList();
    }

    private ProfileSkillResponse mapSkill(ProfileSkill skill) {
        return ProfileSkillResponse.builder()
                .id(skill.getId())
                .skillName(skill.getSkillName())
                .level(skill.getLevel())
                .yearsOfExperience(skill.getYearsOfExperience())
                .build();
    }

    private List<SocialLinkResponse> mapSocialLinks(Profile profile) {
        return profile.getSocialLinks().stream()
                .sorted(Comparator.comparing(link -> link.getPlatform() == null ? "" : link.getPlatform().name()))
                .map(this::mapSocialLink)
                .toList();
    }

    private SocialLinkResponse mapSocialLink(SocialLink link) {
        return SocialLinkResponse.builder()
                .id(link.getId())
                .platform(link.getPlatform())
                .url(link.getUrl())
                .build();
    }
}
