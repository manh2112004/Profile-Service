package org.Profile.command.service.Impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.Profile.command.command.AddEducationToProfileCommand;
import org.Profile.command.command.AddExperienceToProfileCommand;
import org.Profile.command.command.AddPortfolioToProfileCommand;
import org.Profile.command.command.AddSkillToProfileCommand;
import org.Profile.command.command.AddSocialLinkToProfileCommand;
import org.Profile.command.command.CreateProfileCommand;
import org.Profile.command.command.DeleteProfileAvatarCommand;
import org.Profile.command.command.DeleteProfileCoverImageCommand;
import org.Profile.command.command.DeleteProfileEducationCommand;
import org.Profile.command.command.DeleteProfileExperienceCommand;
import org.Profile.command.command.DeleteProfileSkillCommand;
import org.Profile.command.command.DeleteProfileSocialLinkCommand;
import org.Profile.command.command.UpdateProfileEducationCommand;
import org.Profile.command.command.UpdateProfileExperienceCommand;
import org.Profile.command.command.UpdateProfileCommand;
import org.Profile.command.command.UpdateProfileAvatarCommand;
import org.Profile.command.command.UpdateProfileCoverImageCommand;
import org.Profile.command.command.UpdateProfilePortfolioCommand;
import org.Profile.command.command.UpdateProfileSkillCommand;
import org.Profile.command.command.UpdateProfileSocialLinkCommand;
import org.Profile.command.data.Profile;
import org.Profile.command.data.ProfileRepository;
import org.Profile.command.model.request.CreateEducationRequest;
import org.Profile.command.model.request.CreatePortfolioRequest;
import org.Profile.command.model.request.CreateProfileRequest;
import org.Profile.command.model.request.CreateProfileSkillRequest;
import org.Profile.command.model.request.CreateSocialLinkRequest;
import org.Profile.command.model.request.CreateWorkExperienceRequest;
import org.Profile.command.model.request.UpdateProfileRequest;
import org.Profile.command.service.ProfileService;
import org.Profile.constant.ProfileStatus;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class ProfileServiceImpl implements ProfileService {
    @Autowired
    private CommandGateway commandGateway;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private Cloudinary cloudinary;

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
    public CompletableFuture<String> updateAvatar(String userId, String profileId, MultipartFile file) {
        if (userId == null || userId.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Không xác định được user từ token");
        }

        if (file == null || file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Vui lòng chọn ảnh đại diện");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File upload phải là hình ảnh");
        }

        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile không tồn tại"));

        if (!profile.getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền cập nhật profile này");
        }

        String avatarUrl = uploadImage(file, "profile-service/avatars", "Upload avatar thất bại");

        UpdateProfileAvatarCommand command = UpdateProfileAvatarCommand.builder()
                .profileId(profile.getId())
                .avatarUrl(avatarUrl)
                .build();

        return commandGateway.send(command);
    }

    @Override
    public CompletableFuture<String> updateMyCoverImage(String userId, MultipartFile file) {
        if (userId == null || userId.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Không xác định được user từ token");
        }

        if (file == null || file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Vui lòng chọn ảnh bìa");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File upload phải là hình ảnh");
        }

        Profile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile không tồn tại"));

        String coverImageUrl = uploadImage(file, "profile-service/cover-images", "Upload ảnh bìa thất bại");

        UpdateProfileCoverImageCommand command = UpdateProfileCoverImageCommand.builder()
                .profileId(profile.getId())
                .coverImageUrl(coverImageUrl)
                .build();

        return commandGateway.send(command);
    }

    @Override
    public CompletableFuture<String> deleteMyCoverImage(String userId) {
        if (userId == null || userId.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Không xác định được user từ token");
        }

        Profile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile không tồn tại"));

        deleteImageIfPossible(profile.getCoverImageUrl(), "Xóa ảnh bìa trên Cloudinary thất bại");

        return commandGateway.send(new DeleteProfileCoverImageCommand(profile.getId()));
    }

    @Override
    public CompletableFuture<String> deleteAvatar(String userId, String profileId) {
        if (userId == null || userId.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Không xác định được user từ token");
        }

        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile không tồn tại"));

        if (!profile.getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền cập nhật profile này");
        }

        deleteImageIfPossible(profile.getAvatarUrl(), "Xóa avatar trên Cloudinary thất bại");

        return commandGateway.send(new DeleteProfileAvatarCommand(profile.getId()));
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
    public CompletableFuture<String> addExperience(String userId, String profileId, CreateWorkExperienceRequest request) {
        if (userId == null || userId.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Không xác định được user từ token");
        }

        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile không tồn tại"));

        if (!profile.getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền cập nhật profile này");
        }

        AddExperienceToProfileCommand command = AddExperienceToProfileCommand.builder()
                .profileId(profile.getId())
                .experienceId(UUID.randomUUID().toString())
                .companyName(request.getCompanyName())
                .position(request.getPosition())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .currentlyWorking(request.getCurrentlyWorking())
                .description(request.getDescription())
                .build();

        return commandGateway.send(command);
    }

    @Override
    public CompletableFuture<String> addSkill(String userId, String profileId, CreateProfileSkillRequest request) {
        if (userId == null || userId.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Không xác định được user từ token");
        }

        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile không tồn tại"));

        if (!profile.getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền cập nhật profile này");
        }

        AddSkillToProfileCommand command = AddSkillToProfileCommand.builder()
                .profileId(profile.getId())
                .skillId(UUID.randomUUID().toString())
                .skillName(request.getSkillName())
                .level(request.getLevel())
                .yearsOfExperience(request.getYearsOfExperience())
                .build();

        return commandGateway.send(command);
    }

    @Override
    public CompletableFuture<String> addSocialLink(String userId, String profileId, CreateSocialLinkRequest request) {
        if (userId == null || userId.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Không xác định được user từ token");
        }

        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile không tồn tại"));

        if (!profile.getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền cập nhật profile này");
        }

        AddSocialLinkToProfileCommand command = AddSocialLinkToProfileCommand.builder()
                .profileId(profile.getId())
                .socialLinkId(UUID.randomUUID().toString())
                .platform(request.getPlatform())
                .url(request.getUrl())
                .build();

        return commandGateway.send(command);
    }

    @Override
    public CompletableFuture<String> addPortfolio(String userId, CreatePortfolioRequest request) {
        if (userId == null || userId.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Không xác định được user từ token");
        }

        if (request.getTitle() == null || request.getTitle().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "title không được để trống");
        }

        Profile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile không tồn tại"));

        AddPortfolioToProfileCommand command = AddPortfolioToProfileCommand.builder()
                .profileId(profile.getId())
                .portfolioId(UUID.randomUUID().toString())
                .title(request.getTitle().trim())
                .description(request.getDescription())
                .imageUrl(request.getImageUrl())
                .projectUrl(request.getProjectUrl())
                .githubUrl(request.getGithubUrl())
                .role(request.getRole())
                .organization(request.getOrganization())
                .technologies(request.getTechnologies())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .currentlyWorking(request.getCurrentlyWorking())
                .isPublic(request.getIsPublic())
                .displayOrder(request.getDisplayOrder())
                .build();

        return commandGateway.send(command);
    }

    @Override
    @Transactional(readOnly = true)
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
    @Transactional(readOnly = true)
    public CompletableFuture<String> updateExperience(
            String userId,
            String profileId,
            String experienceId,
            CreateWorkExperienceRequest request
    ) {
        if (userId == null || userId.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Không xác định được user từ token");
        }

        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile không tồn tại"));

        if (!profile.getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền cập nhật profile này");
        }

        boolean experienceExists = profile.getExperiences().stream()
                .anyMatch(experience -> experience.getId().equals(experienceId));

        if (!experienceExists) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Kinh nghiệm làm việc không tồn tại");
        }

        UpdateProfileExperienceCommand command = UpdateProfileExperienceCommand.builder()
                .profileId(profile.getId())
                .experienceId(experienceId)
                .companyName(request.getCompanyName())
                .position(request.getPosition())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .currentlyWorking(request.getCurrentlyWorking())
                .description(request.getDescription())
                .build();

        return commandGateway.send(command);
    }

    @Override
    @Transactional(readOnly = true)
    public CompletableFuture<String> updateSkill(
            String userId,
            String profileId,
            String skillId,
            CreateProfileSkillRequest request
    ) {
        if (userId == null || userId.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Không xác định được user từ token");
        }

        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile không tồn tại"));

        if (!profile.getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền cập nhật profile này");
        }

        boolean skillExists = profile.getSkills().stream()
                .anyMatch(skill -> skill.getId().equals(skillId));

        if (!skillExists) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Kỹ năng không tồn tại");
        }

        UpdateProfileSkillCommand command = UpdateProfileSkillCommand.builder()
                .profileId(profile.getId())
                .skillId(skillId)
                .skillName(request.getSkillName())
                .level(request.getLevel())
                .yearsOfExperience(request.getYearsOfExperience())
                .build();

        return commandGateway.send(command);
    }

    @Override
    @Transactional(readOnly = true)
    public CompletableFuture<String> updateSocialLink(
            String userId,
            String profileId,
            String socialLinkId,
            CreateSocialLinkRequest request
    ) {
        if (userId == null || userId.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Không xác định được user từ token");
        }

        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile không tồn tại"));

        if (!profile.getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền cập nhật profile này");
        }

        boolean socialLinkExists = profile.getSocialLinks().stream()
                .anyMatch(socialLink -> socialLink.getId().equals(socialLinkId));

        if (!socialLinkExists) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Liên kết mạng xã hội không tồn tại");
        }

        UpdateProfileSocialLinkCommand command = UpdateProfileSocialLinkCommand.builder()
                .profileId(profile.getId())
                .socialLinkId(socialLinkId)
                .platform(request.getPlatform())
                .url(request.getUrl())
                .build();

        return commandGateway.send(command);
    }

    @Override
    @Transactional(readOnly = true)
    public CompletableFuture<String> updatePortfolio(String userId, String portfolioId, CreatePortfolioRequest request) {
        if (userId == null || userId.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Không xác định được user từ token");
        }

        if (request.getTitle() != null && request.getTitle().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "title không được để trống");
        }

        Profile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile không tồn tại"));

        boolean portfolioExists = profile.getPortfolios().stream()
                .anyMatch(portfolio -> portfolio.getId().equals(portfolioId));

        if (!portfolioExists) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Portfolio không tồn tại");
        }

        UpdateProfilePortfolioCommand command = UpdateProfilePortfolioCommand.builder()
                .profileId(profile.getId())
                .portfolioId(portfolioId)
                .title(request.getTitle() == null ? null : request.getTitle().trim())
                .description(request.getDescription())
                .imageUrl(request.getImageUrl())
                .projectUrl(request.getProjectUrl())
                .githubUrl(request.getGithubUrl())
                .role(request.getRole())
                .organization(request.getOrganization())
                .technologies(request.getTechnologies())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .currentlyWorking(request.getCurrentlyWorking())
                .isPublic(request.getIsPublic())
                .displayOrder(request.getDisplayOrder())
                .build();

        return commandGateway.send(command);
    }

    @Override
    @Transactional(readOnly = true)
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

    @Override
    @Transactional(readOnly = true)
    public CompletableFuture<String> deleteExperience(String userId, String profileId, String experienceId) {
        if (userId == null || userId.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Không xác định được user từ token");
        }

        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile không tồn tại"));

        if (!profile.getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền cập nhật profile này");
        }

        boolean experienceExists = profile.getExperiences().stream()
                .anyMatch(experience -> experience.getId().equals(experienceId));

        if (!experienceExists) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Kinh nghiệm làm việc không tồn tại");
        }

        return commandGateway.send(new DeleteProfileExperienceCommand(profile.getId(), experienceId));
    }

    @Override
    @Transactional(readOnly = true)
    public CompletableFuture<String> deleteSkill(String userId, String profileId, String skillId) {
        if (userId == null || userId.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Không xác định được user từ token");
        }

        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile không tồn tại"));

        if (!profile.getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền cập nhật profile này");
        }

        boolean skillExists = profile.getSkills().stream()
                .anyMatch(skill -> skill.getId().equals(skillId));

        if (!skillExists) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Kỹ năng không tồn tại");
        }

        return commandGateway.send(new DeleteProfileSkillCommand(profile.getId(), skillId));
    }

    @Override
    @Transactional(readOnly = true)
    public CompletableFuture<String> deleteSocialLink(String userId, String profileId, String socialLinkId) {
        if (userId == null || userId.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Không xác định được user từ token");
        }

        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile không tồn tại"));

        if (!profile.getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền cập nhật profile này");
        }

        boolean socialLinkExists = profile.getSocialLinks().stream()
                .anyMatch(socialLink -> socialLink.getId().equals(socialLinkId));

        if (!socialLinkExists) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Liên kết mạng xã hội không tồn tại");
        }

        return commandGateway.send(new DeleteProfileSocialLinkCommand(profile.getId(), socialLinkId));
    }

    private String uploadImage(MultipartFile file, String folder, String errorMessage) {
        try {
            Map<?, ?> result = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "folder", folder,
                    "resource_type", "image"
            ));
            Object secureUrl = result.get("secure_url");
            if (secureUrl == null) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Cloudinary không trả về URL ảnh");
            }
            return secureUrl.toString();
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage, e);
        }
    }

    private void deleteImageIfPossible(String imageUrl, String errorMessage) {
        String publicId = extractCloudinaryPublicId(imageUrl);
        if (publicId == null) {
            return;
        }

        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage, e);
        }
    }

    private String extractCloudinaryPublicId(String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank()) {
            return null;
        }

        try {
            String path = URI.create(imageUrl).getPath();
            String marker = "/upload/";
            int uploadIndex = path.indexOf(marker);
            if (uploadIndex < 0) {
                return null;
            }

            String publicPath = path.substring(uploadIndex + marker.length());
            publicPath = publicPath.replaceFirst("^v\\d+/", "");
            int extensionIndex = publicPath.lastIndexOf('.');
            if (extensionIndex > 0) {
                publicPath = publicPath.substring(0, extensionIndex);
            }
            return publicPath.isBlank() ? null : publicPath;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
