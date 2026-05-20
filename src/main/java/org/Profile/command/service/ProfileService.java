package org.Profile.command.service;

import org.Profile.command.model.request.CreateProfileRequest;
import org.Profile.command.model.request.UpdateProfileRequest;

import java.util.concurrent.CompletableFuture;

public interface ProfileService {
    CompletableFuture<String> createProfile(String userId, CreateProfileRequest request);

    CompletableFuture<String> updateMyProfile(String userId, UpdateProfileRequest request);
}
