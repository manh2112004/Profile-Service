package org.Profile.query.queries;

import org.Profile.command.data.Education;
import org.Profile.command.data.Portfolio;
import org.Profile.command.data.Profile;
import org.Profile.command.data.ProfileRepository;
import org.Profile.command.data.ProfileSkill;
import org.Profile.command.data.SocialLink;
import org.Profile.command.data.WorkExperience;
import org.Profile.constant.ProfileStatus;
import org.Profile.query.model.response.EducationResponse;
import org.Profile.query.model.response.ProfileCompletionResponse;
import org.Profile.query.model.response.ProfileCompletionSectionResponse;
import org.Profile.query.model.response.PortfolioResponse;
import org.Profile.query.model.response.ProfileResponse;
import org.Profile.query.model.response.ProfileSkillResponse;
import org.Profile.query.model.response.PublicProfileResponse;
import org.Profile.query.model.response.SocialLinkResponse;
import org.Profile.query.model.response.WorkExperienceResponse;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.ArrayList;
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
    public List<PortfolioResponse> handle(GetMyProfilePortfoliosQuery query) {
        Profile profile = profileRepository.findByUserId(query.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile không tồn tại"));
        return mapPortfolios(profile);
    }

    @QueryHandler
    @Transactional(readOnly = true)
    public ProfileCompletionResponse handle(GetMyProfileCompletionQuery query) {
        Profile profile = profileRepository.findByUserId(query.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile không tồn tại"));
        return calculateCompletion(profile);
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
    public PublicProfileResponse handle(GetPublicProfileQuery query) {
        Profile profile = profileRepository.findById(query.getProfileId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile không tồn tại"));

        if (profile.getStatus() != ProfileStatus.ACTIVE) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile không tồn tại");
        }

        return mapToPublicResponse(profile);
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
                .coverImageUrl(profile.getCoverImageUrl())
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

    private PublicProfileResponse mapToPublicResponse(Profile profile) {
        return PublicProfileResponse.builder()
                .id(profile.getId())
                .fullName(profile.getFullName())
                .avatarUrl(profile.getAvatarUrl())
                .coverImageUrl(profile.getCoverImageUrl())
                .city(profile.getCity())
                .country(profile.getCountry())
                .headline(profile.getHeadline())
                .summary(profile.getSummary())
                .currentPosition(profile.getCurrentPosition())
                .currentCompany(profile.getCurrentCompany())
                .yearsOfExperience(profile.getYearsOfExperience())
                .expectedJobTitle(profile.getExpectedJobTitle())
                .expectedLocation(profile.getExpectedLocation())
                .status(profile.getStatus())
                .educations(mapEducations(profile))
                .experiences(mapExperiences(profile))
                .skills(mapSkills(profile))
                .socialLinks(mapSocialLinks(profile))
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

    private List<PortfolioResponse> mapPortfolios(Profile profile) {
        return profile.getPortfolios().stream()
                .sorted(Comparator
                        .comparing(Portfolio::getDisplayOrder, Comparator.nullsLast(Integer::compareTo))
                        .thenComparing(Portfolio::getUpdatedAt, Comparator.nullsLast(Comparator.reverseOrder())))
                .map(this::mapPortfolio)
                .toList();
    }

    private PortfolioResponse mapPortfolio(Portfolio portfolio) {
        return PortfolioResponse.builder()
                .id(portfolio.getId())
                .title(portfolio.getTitle())
                .description(portfolio.getDescription())
                .imageUrl(portfolio.getImageUrl())
                .projectUrl(portfolio.getProjectUrl())
                .githubUrl(portfolio.getGithubUrl())
                .role(portfolio.getRole())
                .organization(portfolio.getOrganization())
                .technologies(portfolio.getTechnologies())
                .startDate(portfolio.getStartDate())
                .endDate(portfolio.getEndDate())
                .currentlyWorking(portfolio.getCurrentlyWorking())
                .isPublic(portfolio.getIsPublic())
                .displayOrder(portfolio.getDisplayOrder())
                .createdAt(portfolio.getCreatedAt())
                .updatedAt(portfolio.getUpdatedAt())
                .build();
    }

    private ProfileCompletionResponse calculateCompletion(Profile profile) {
        List<ProfileCompletionSectionResponse> sections = List.of(
                buildSection("basic", List.of(
                        field("fullName", hasText(profile.getFullName())),
                        field("phoneNumber", hasText(profile.getPhoneNumber())),
                        field("dateOfBirth", profile.getDateOfBirth() != null),
                        field("gender", profile.getGender() != null)
                )),
                buildSection("location", List.of(
                        field("address", hasText(profile.getAddress())),
                        field("city", hasText(profile.getCity())),
                        field("country", hasText(profile.getCountry()))
                )),
                buildSection("media", List.of(
                        field("avatarUrl", hasText(profile.getAvatarUrl())),
                        field("coverImageUrl", hasText(profile.getCoverImageUrl()))
                )),
                buildSection("career", List.of(
                        field("headline", hasText(profile.getHeadline())),
                        field("summary", hasText(profile.getSummary())),
                        field("currentPosition", hasText(profile.getCurrentPosition())),
                        field("currentCompany", hasText(profile.getCurrentCompany())),
                        field("yearsOfExperience", profile.getYearsOfExperience() != null)
                )),
                buildSection("expectation", List.of(
                        field("expectedJobTitle", hasText(profile.getExpectedJobTitle())),
                        field("expectedLocation", hasText(profile.getExpectedLocation())),
                        field("expectedSalary", profile.getExpectedSalary() != null)
                )),
                buildSection("profileDetails", List.of(
                        field("educations", !profile.getEducations().isEmpty()),
                        field("experiences", !profile.getExperiences().isEmpty()),
                        field("skills", !profile.getSkills().isEmpty()),
                        field("socialLinks", !profile.getSocialLinks().isEmpty()),
                        field("portfolios", !profile.getPortfolios().isEmpty())
                ))
        );

        int totalItems = sections.stream()
                .mapToInt(ProfileCompletionSectionResponse::getTotalItems)
                .sum();
        int completedItems = sections.stream()
                .mapToInt(ProfileCompletionSectionResponse::getCompletedItems)
                .sum();
        List<String> missingFields = sections.stream()
                .flatMap(section -> section.getMissingFields().stream())
                .toList();

        return ProfileCompletionResponse.builder()
                .completionPercentage(calculatePercentage(completedItems, totalItems))
                .completedItems(completedItems)
                .totalItems(totalItems)
                .missingFields(missingFields)
                .sections(sections)
                .build();
    }

    private ProfileCompletionSectionResponse buildSection(String section, List<CompletionField> fields) {
        int totalItems = fields.size();
        int completedItems = (int) fields.stream()
                .filter(CompletionField::completed)
                .count();
        List<String> missingFields = fields.stream()
                .filter(field -> !field.completed())
                .map(CompletionField::name)
                .toList();

        return ProfileCompletionSectionResponse.builder()
                .section(section)
                .completionPercentage(calculatePercentage(completedItems, totalItems))
                .completedItems(completedItems)
                .totalItems(totalItems)
                .missingFields(new ArrayList<>(missingFields))
                .build();
    }

    private CompletionField field(String name, boolean completed) {
        return new CompletionField(name, completed);
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    private int calculatePercentage(int completedItems, int totalItems) {
        if (totalItems == 0) {
            return 0;
        }
        return (int) Math.round((completedItems * 100.0) / totalItems);
    }

    private record CompletionField(String name, boolean completed) {
    }
}
