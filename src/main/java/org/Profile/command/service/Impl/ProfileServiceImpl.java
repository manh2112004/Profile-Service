package org.Profile.command.service.Impl;

import org.Profile.command.command.CreateProfileCommand;
import org.Profile.command.command.UpdateProfileCommand;
import org.Profile.command.data.Profile;
import org.Profile.command.data.ProfileRepository;
import org.Profile.command.model.request.CreateProfileRequest;
import org.Profile.command.model.request.UpdateProfileRequest;
import org.Profile.command.service.ProfileService;
import org.Profile.constant.ProfileStatus;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class ProfileServiceImpl implements ProfileService {
    @Autowired
    private CommandGateway commandGateway;

    @Autowired
    private ProfileRepository profileRepository;

    @Override
    public CompletableFuture<String> createProfile(String userId, CreateProfileRequest request) {
        if (userId == null || userId.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Không xác định được user từ token");
        }

        if (profileRepository.existsByUserId(userId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Profile đã tồn tại");
        }

        CreateProfileCommand command = CreateProfileCommand.builder()
                .id(UUID.randomUUID().toString())
                .userId(userId)
                .fullName(request.getFullName().trim())
                .avatarUrl(request.getAvatarUrl())
                .phoneNumber(request.getPhoneNumber())
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .address(request.getAddress())
                .city(request.getCity())
                .country(request.getCountry())
                .headline(request.getHeadline())
                .summary(request.getSummary())
                .currentPosition(request.getCurrentPosition())
                .currentCompany(request.getCurrentCompany())
                .yearsOfExperience(request.getYearsOfExperience())
                .expectedJobTitle(request.getExpectedJobTitle())
                .expectedLocation(request.getExpectedLocation())
                .expectedSalary(request.getExpectedSalary())
                .status(ProfileStatus.ACTIVE)
                .educations(request.getEducations() == null ? Collections.emptyList() : request.getEducations())
                .experiences(request.getExperiences() == null ? Collections.emptyList() : request.getExperiences())
                .skills(request.getSkills() == null ? Collections.emptyList() : request.getSkills())
                .socialLinks(request.getSocialLinks() == null ? Collections.emptyList() : request.getSocialLinks())
                .build();

        return commandGateway.send(command);
    }

    @Override
    public CompletableFuture<String> updateMyProfile(String userId, UpdateProfileRequest request) {
        if (userId == null || userId.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Không xác định được user từ token");
        }

        Profile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile không tồn tại"));

        if (request.getFullName() != null && request.getFullName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "fullName không được để trống");
        }

        UpdateProfileCommand command = UpdateProfileCommand.builder()
                .id(profile.getId())
                .fullName(request.getFullName() == null ? null : request.getFullName().trim())
                .avatarUrl(request.getAvatarUrl())
                .phoneNumber(request.getPhoneNumber())
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .address(request.getAddress())
                .city(request.getCity())
                .country(request.getCountry())
                .headline(request.getHeadline())
                .summary(request.getSummary())
                .currentPosition(request.getCurrentPosition())
                .currentCompany(request.getCurrentCompany())
                .yearsOfExperience(request.getYearsOfExperience())
                .expectedJobTitle(request.getExpectedJobTitle())
                .expectedLocation(request.getExpectedLocation())
                .expectedSalary(request.getExpectedSalary())
                .educations(request.getEducations())
                .experiences(request.getExperiences())
                .skills(request.getSkills())
                .socialLinks(request.getSocialLinks())
                .build();

        return commandGateway.send(command);
    }
}
