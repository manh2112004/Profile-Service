package org.Profile.query.controller;

import org.Profile.query.model.response.ProfileResponse;
import org.Profile.query.model.response.EducationResponse;
import org.Profile.query.model.response.ProfileSkillResponse;
import org.Profile.query.model.response.WorkExperienceResponse;
import org.Profile.query.model.response.SocialLinkResponse;
import org.Profile.query.queries.GetMyProfileQuery;
import org.Profile.query.queries.GetProfileByIdQuery;
import org.Profile.query.queries.GetProfileEducationDetailQuery;
import org.Profile.query.queries.GetProfileEducationsQuery;
import org.Profile.query.queries.GetProfileExperienceDetailQuery;
import org.Profile.query.queries.GetProfileExperiencesQuery;
import org.Profile.query.queries.GetProfileSkillsQuery;
import org.Profile.query.queries.GetProfileSocialLinksQuery;
import org.Profile.query.queries.SearchProfilesQuery;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.List;

@RestController
@RequestMapping("/api/v1/profiles")
public class ProfileQueryController {
    @Autowired
    private QueryGateway queryGateway;

    @GetMapping("/me")
    public CompletableFuture<ProfileResponse> getMyProfile(@AuthenticationPrincipal Jwt jwt) {
        return queryGateway.query(
                new GetMyProfileQuery(jwt.getSubject()),
                ResponseTypes.instanceOf(ProfileResponse.class)
        );
    }

    @GetMapping("/search")
    public CompletableFuture<List<ProfileResponse>> searchProfiles(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String skill
    ) {
        return queryGateway.query(
                new SearchProfilesQuery(normalize(keyword), normalize(city), normalize(country), normalize(skill)),
                ResponseTypes.multipleInstancesOf(ProfileResponse.class)
        );
    }

    @GetMapping("/{profileId}")
    public CompletableFuture<ProfileResponse> getProfileById(@PathVariable String profileId) {
        return queryGateway.query(
                new GetProfileByIdQuery(profileId),
                ResponseTypes.instanceOf(ProfileResponse.class)
        );
    }

    @GetMapping("/{profileId}/educations")
    public CompletableFuture<List<EducationResponse>> getProfileEducations(@PathVariable String profileId) {
        return queryGateway.query(
                new GetProfileEducationsQuery(profileId),
                ResponseTypes.multipleInstancesOf(EducationResponse.class)
        );
    }

    @GetMapping("/{profileId}/experiences")
    public CompletableFuture<List<WorkExperienceResponse>> getProfileExperiences(@PathVariable String profileId) {
        return queryGateway.query(
                new GetProfileExperiencesQuery(profileId),
                ResponseTypes.multipleInstancesOf(WorkExperienceResponse.class)
        );
    }

    @GetMapping("/{profileId}/skills")
    public CompletableFuture<List<ProfileSkillResponse>> getProfileSkills(@PathVariable String profileId) {
        return queryGateway.query(
                new GetProfileSkillsQuery(profileId),
                ResponseTypes.multipleInstancesOf(ProfileSkillResponse.class)
        );
    }

    @GetMapping("/{profileId}/social-links")
    public CompletableFuture<List<SocialLinkResponse>> getProfileSocialLinks(@PathVariable String profileId) {
        return queryGateway.query(
                new GetProfileSocialLinksQuery(profileId),
                ResponseTypes.multipleInstancesOf(SocialLinkResponse.class)
        );
    }

    @GetMapping("/{profileId}/experiences/{experienceId}")
    public CompletableFuture<WorkExperienceResponse> getProfileExperienceDetail(
            @PathVariable String profileId,
            @PathVariable String experienceId
    ) {
        return queryGateway.query(
                new GetProfileExperienceDetailQuery(profileId, experienceId),
                ResponseTypes.instanceOf(WorkExperienceResponse.class)
        );
    }

    @GetMapping("/{profileId}/educations/{educationId}")
    public CompletableFuture<EducationResponse> getProfileEducationDetail(
            @PathVariable String profileId,
            @PathVariable String educationId
    ) {
        return queryGateway.query(
                new GetProfileEducationDetailQuery(profileId, educationId),
                ResponseTypes.instanceOf(EducationResponse.class)
        );
    }

    private String normalize(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
