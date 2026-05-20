package org.Profile.command.service;

import org.Profile.command.model.request.CreateProfileRequest;
import org.Profile.command.model.request.CreateEducationRequest;
import org.Profile.command.model.request.UpdateProfileRequest;

import java.util.concurrent.CompletableFuture;

public interface ProfileService {
    CompletableFuture<String> createProfile(String userId, CreateProfileRequest request);

    CompletableFuture<String> updateMyProfile(String userId, UpdateProfileRequest request);

    CompletableFuture<String> addEducation(String userId, String profileId, CreateEducationRequest request);

    CompletableFuture<String> updateEducation(
            String userId,
            String profileId,
            String educationId,
            CreateEducationRequest request
    );
}
