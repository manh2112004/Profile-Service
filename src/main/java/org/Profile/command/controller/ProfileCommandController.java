package org.Profile.command.controller;

import jakarta.validation.Valid;
import org.Profile.command.model.request.CreateEducationRequest;
import org.Profile.command.model.request.CreateProfileRequest;
import org.Profile.command.model.request.CreateWorkExperienceRequest;
import org.Profile.command.model.request.UpdateProfileRequest;
import org.Profile.command.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/profiles")
public class ProfileCommandController {
    @Autowired
    private ProfileService profileService;

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
        return profileService.updateMyProfile(jwt.getSubject(), request);
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
}
