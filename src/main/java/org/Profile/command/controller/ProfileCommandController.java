package org.Profile.command.controller;

import jakarta.validation.Valid;
import org.Profile.command.model.request.CreateEducationRequest;
import org.Profile.command.model.request.CreatePortfolioRequest;
import org.Profile.command.model.request.CreateProfileRequest;
import org.Profile.command.model.request.CreateProfileSkillRequest;
import org.Profile.command.model.request.CreateSocialLinkRequest;
import org.Profile.command.model.request.CreateWorkExperienceRequest;
import org.Profile.command.model.request.UpdateProfileRequest;
import org.Profile.command.service.ProfileService;
import org.Profile.event.KafkaEvent;
import org.Profile.event.KafkaEventProducer;
import org.Profile.event.KafkaTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/profiles")
public class ProfileCommandController {
    @Autowired
    private ProfileService profileService;

    @Autowired
    private KafkaEventProducer kafkaEventProducer;

    @PostMapping
    public CompletableFuture<String> createProfile(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody CreateProfileRequest request
    ) {
        return profileService.createProfile(jwt.getSubject(), request);
    }

    @PutMapping("/me")
    public CompletableFuture<String> updateMyProfile(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody UpdateProfileRequest request
    ) {
        return profileService.updateMyProfile(jwt.getSubject(), request).thenApply(result -> {
            kafkaEventProducer.sendEvent(KafkaTopic.PROFILE_EVENTS, KafkaEvent.builder()
                    .eventId(UUID.randomUUID().toString())
                    .eventType("ProfileUpdatedEvent")
                    .userId(jwt.getSubject())
                    .referenceId(jwt.getSubject())
                    .referenceType("PROFILE")
                    .title("Cập nhật hồ sơ")
                    .message("Hồ sơ cá nhân của bạn đã được cập nhật thành công.")
                    .createdAt(LocalDateTime.now())
                    .build());
            return result;
        });
    }

    @PutMapping("/{id}")
    public CompletableFuture<String> updateProfileById(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String id,
            @Valid @RequestBody UpdateProfileRequest request
    ) {
        return profileService.updateMyProfile(jwt.getSubject(), request).thenApply(result -> {
            kafkaEventProducer.sendEvent(KafkaTopic.PROFILE_EVENTS, KafkaEvent.builder()
                    .eventId(UUID.randomUUID().toString())
                    .eventType("ProfileUpdatedEvent")
                    .userId(jwt.getSubject())
                    .referenceId(id)
                    .referenceType("PROFILE")
                    .title("Cập nhật hồ sơ")
                    .message("Hồ sơ cá nhân của bạn đã được cập nhật thành công.")
                    .createdAt(LocalDateTime.now())
                    .build());
            return result;
        });
    }

    @PutMapping("/{id}/complete")
    public CompletableFuture<String> completeProfile(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String id
    ) {
        kafkaEventProducer.sendEvent(KafkaTopic.PROFILE_EVENTS, KafkaEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .eventType("ProfileCompletedEvent")
                .userId(jwt.getSubject())
                .referenceId(id)
                .referenceType("PROFILE")
                .title("Hoàn thành hồ sơ")
                .message("Chúc mừng! Bạn đã hoàn thành hồ sơ cá nhân.")
                .createdAt(LocalDateTime.now())
                .build());
        return CompletableFuture.completedFuture("Hoàn thành hồ sơ thành công");
    }

    @PostMapping(value = "/{profileId}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CompletableFuture<String> updateAvatar(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String profileId,
            @RequestParam("file") MultipartFile file
    ) {
        return profileService.updateAvatar(jwt.getSubject(), profileId, file);
    }

    @PostMapping(value = "/me/cover-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CompletableFuture<String> updateMyCoverImage(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam("file") MultipartFile file
    ) {
        return profileService.updateMyCoverImage(jwt.getSubject(), file);
    }

    @DeleteMapping("/me/cover-image")
    public CompletableFuture<String> deleteMyCoverImage(@AuthenticationPrincipal Jwt jwt) {
        return profileService.deleteMyCoverImage(jwt.getSubject());
    }

    @DeleteMapping("/{profileId}/avatar")
    public CompletableFuture<String> deleteAvatar(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String profileId
    ) {
        return profileService.deleteAvatar(jwt.getSubject(), profileId);
    }

    @PostMapping("/{profileId}/educations")
    public CompletableFuture<String> addEducation(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String profileId,
            @RequestBody CreateEducationRequest request
    ) {
        return profileService.addEducation(jwt.getSubject(), profileId, request);
    }

    @PostMapping("/{profileId}/experiences")
    public CompletableFuture<String> addExperience(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String profileId,
            @RequestBody CreateWorkExperienceRequest request
    ) {
        return profileService.addExperience(jwt.getSubject(), profileId, request);
    }

    @PostMapping("/{profileId}/skills")
    public CompletableFuture<String> addSkill(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String profileId,
            @RequestBody CreateProfileSkillRequest request
    ) {
        return profileService.addSkill(jwt.getSubject(), profileId, request);
    }

    @PostMapping("/{profileId}/social-links")
    public CompletableFuture<String> addSocialLink(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String profileId,
            @RequestBody CreateSocialLinkRequest request
    ) {
        return profileService.addSocialLink(jwt.getSubject(), profileId, request);
    }

    @PostMapping(value = "/me/portfolios", consumes = MediaType.APPLICATION_JSON_VALUE)
    public CompletableFuture<String> addPortfolio(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody CreatePortfolioRequest request
    ) {
        return profileService.addPortfolio(jwt.getSubject(), request);
    }

    @PostMapping(value = "/me/portfolios", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CompletableFuture<String> addPortfolioWithImage(
            @AuthenticationPrincipal Jwt jwt,
            @ModelAttribute CreatePortfolioRequest request,
            @RequestParam(value = "image", required = false) MultipartFile image
    ) {
        return profileService.addPortfolio(jwt.getSubject(), request, image);
    }

    @PutMapping("/{profileId}/educations/{educationId}")
    public CompletableFuture<String> updateEducation(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String profileId,
            @PathVariable String educationId,
            @RequestBody CreateEducationRequest request
    ) {
        return profileService.updateEducation(jwt.getSubject(), profileId, educationId, request);
    }

    @PutMapping("/{profileId}/experiences/{experienceId}")
    public CompletableFuture<String> updateExperience(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String profileId,
            @PathVariable String experienceId,
            @RequestBody CreateWorkExperienceRequest request
    ) {
        return profileService.updateExperience(jwt.getSubject(), profileId, experienceId, request);
    }

    @PutMapping("/{profileId}/skills/{skillId}")
    public CompletableFuture<String> updateSkill(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String profileId,
            @PathVariable String skillId,
            @RequestBody CreateProfileSkillRequest request
    ) {
        return profileService.updateSkill(jwt.getSubject(), profileId, skillId, request);
    }

    @PutMapping("/{profileId}/social-links/{socialLinkId}")
    public CompletableFuture<String> updateSocialLink(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String profileId,
            @PathVariable String socialLinkId,
            @RequestBody CreateSocialLinkRequest request
    ) {
        return profileService.updateSocialLink(jwt.getSubject(), profileId, socialLinkId, request);
    }

    @PutMapping(value = "/me/portfolios/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public CompletableFuture<String> updatePortfolio(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String id,
            @RequestBody CreatePortfolioRequest request
    ) {
        return profileService.updatePortfolio(jwt.getSubject(), id, request);
    }

    @PutMapping(value = "/me/portfolios/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CompletableFuture<String> updatePortfolioWithImage(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String id,
            @ModelAttribute CreatePortfolioRequest request,
            @RequestParam(value = "image", required = false) MultipartFile image
    ) {
        return profileService.updatePortfolio(jwt.getSubject(), id, request, image);
    }

    @DeleteMapping("/{profileId}/educations/{educationId}")
    public CompletableFuture<String> deleteEducation(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String profileId,
            @PathVariable String educationId
    ) {
        return profileService.deleteEducation(jwt.getSubject(), profileId, educationId);
    }

    @DeleteMapping("/{profileId}/experiences/{experienceId}")
    public CompletableFuture<String> deleteExperience(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String profileId,
            @PathVariable String experienceId
    ) {
        return profileService.deleteExperience(jwt.getSubject(), profileId, experienceId);
    }

    @DeleteMapping("/{profileId}/skills/{skillId}")
    public CompletableFuture<String> deleteSkill(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String profileId,
            @PathVariable String skillId
    ) {
        return profileService.deleteSkill(jwt.getSubject(), profileId, skillId);
    }

    @DeleteMapping("/{profileId}/social-links/{socialLinkId}")
    public CompletableFuture<String> deleteSocialLink(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String profileId,
            @PathVariable String socialLinkId
    ) {
        return profileService.deleteSocialLink(jwt.getSubject(), profileId, socialLinkId);
    }

    @DeleteMapping("/me/portfolios/{id}")
    public CompletableFuture<String> deletePortfolio(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String id
    ) {
        return profileService.deletePortfolio(jwt.getSubject(), id);
    }
}
