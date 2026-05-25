package org.Profile.command.service;

import org.Profile.command.model.request.CreateProfileRequest;
import org.Profile.command.model.request.CreateEducationRequest;
import org.Profile.command.model.request.CreateProfileSkillRequest;
import org.Profile.command.model.request.CreateSocialLinkRequest;
import org.Profile.command.model.request.UpdateProfileRequest;
import org.Profile.command.model.request.CreateWorkExperienceRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.CompletableFuture;

public interface ProfileService {
    CompletableFuture<String> createProfile(String userId, CreateProfileRequest request);

    CompletableFuture<String> updateMyProfile(String userId, UpdateProfileRequest request);

    CompletableFuture<String> updateAvatar(String userId, String profileId, MultipartFile file);

    CompletableFuture<String> updateMyCoverImage(String userId, MultipartFile file);

    CompletableFuture<String> deleteMyCoverImage(String userId);

    CompletableFuture<String> deleteAvatar(String userId, String profileId);

    CompletableFuture<String> addEducation(String userId, String profileId, CreateEducationRequest request);

    CompletableFuture<String> addExperience(String userId, String profileId, CreateWorkExperienceRequest request);

    CompletableFuture<String> addSkill(String userId, String profileId, CreateProfileSkillRequest request);

    CompletableFuture<String> addSocialLink(String userId, String profileId, CreateSocialLinkRequest request);

    CompletableFuture<String> updateEducation(
            String userId,
            String profileId,
            String educationId,
            CreateEducationRequest request
    );

    CompletableFuture<String> updateExperience(
            String userId,
            String profileId,
            String experienceId,
            CreateWorkExperienceRequest request
    );

    CompletableFuture<String> updateSkill(
            String userId,
            String profileId,
            String skillId,
            CreateProfileSkillRequest request
    );

    CompletableFuture<String> updateSocialLink(
            String userId,
            String profileId,
            String socialLinkId,
            CreateSocialLinkRequest request
    );

    CompletableFuture<String> deleteEducation(String userId, String profileId, String educationId);

    CompletableFuture<String> deleteExperience(String userId, String profileId, String experienceId);

    CompletableFuture<String> deleteSkill(String userId, String profileId, String skillId);

    CompletableFuture<String> deleteSocialLink(String userId, String profileId, String socialLinkId);
}
