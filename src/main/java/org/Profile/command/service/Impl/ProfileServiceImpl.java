package org.Profile.command.service.Impl;

import org.Profile.command.command.AddEducationToProfileCommand;
import org.Profile.command.command.CreateProfileCommand;
import org.Profile.command.command.DeleteProfileEducationCommand;
import org.Profile.command.command.UpdateProfileEducationCommand;
import org.Profile.command.command.UpdateProfileCommand;
import org.Profile.command.data.Profile;
import org.Profile.command.data.ProfileRepository;
import org.Profile.command.model.request.CreateEducationRequest;
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

    @Override
    public CompletableFuture<String> addEducation(String userId, String profileId, CreateEducationRequest request) {
        if (userId == null || userId.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Không xác định được user từ token");
        }

        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile không tồn tại"));

        if (!profile.getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền cập nhật profile này");
        }

        AddEducationToProfileCommand command = AddEducationToProfileCommand.builder()
                .profileId(profile.getId())
                .educationId(UUID.randomUUID().toString())
                .schoolName(request.getSchoolName())
                .degree(request.getDegree())
                .fieldOfStudy(request.getFieldOfStudy())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .currentlyStudying(request.getCurrentlyStudying())
                .description(request.getDescription())
                .build();

        return commandGateway.send(command);
    }

    @Override
    public CompletableFuture<String> updateEducation(
            String userId,
            String profileId,
            String educationId,
            CreateEducationRequest request
    ) {
        if (userId == null || userId.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Không xác định được user từ token");
        }

        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile không tồn tại"));

        if (!profile.getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền cập nhật profile này");
        }

        boolean educationExists = profile.getEducations().stream()
                .anyMatch(education -> education.getId().equals(educationId));

        if (!educationExists) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Học vấn không tồn tại");
        }

        UpdateProfileEducationCommand command = UpdateProfileEducationCommand.builder()
                .profileId(profile.getId())
                .educationId(educationId)
                .schoolName(request.getSchoolName())
                .degree(request.getDegree())
                .fieldOfStudy(request.getFieldOfStudy())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .currentlyStudying(request.getCurrentlyStudying())
                .description(request.getDescription())
                .build();

        return commandGateway.send(command);
    }

    @Override
    public CompletableFuture<String> deleteEducation(String userId, String profileId, String educationId) {
        if (userId == null || userId.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Không xác định được user từ token");
        }

        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile không tồn tại"));

        if (!profile.getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền cập nhật profile này");
        }

        boolean educationExists = profile.getEducations().stream()
                .anyMatch(education -> education.getId().equals(educationId));

        if (!educationExists) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Học vấn không tồn tại");
        }

        return commandGateway.send(new DeleteProfileEducationCommand(profile.getId(), educationId));
    }
}
