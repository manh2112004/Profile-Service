package org.Profile.command.service;

import org.Profile.command.model.request.CreateProfileRequest;

import java.util.concurrent.CompletableFuture;

public interface ProfileService {
    CompletableFuture<String> createProfile(String userId, CreateProfileRequest request);
}
