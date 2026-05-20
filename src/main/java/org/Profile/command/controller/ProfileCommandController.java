package org.Profile.command.controller;

import jakarta.validation.Valid;
import org.Profile.command.model.request.CreateProfileRequest;
import org.Profile.command.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
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
}
