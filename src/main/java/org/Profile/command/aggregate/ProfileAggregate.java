package org.Profile.command.aggregate;

import org.Profile.command.command.AddEducationToProfileCommand;
import org.Profile.command.command.AddExperienceToProfileCommand;
import org.Profile.command.command.AddSkillToProfileCommand;
import org.Profile.command.command.AddSocialLinkToProfileCommand;
import org.Profile.command.command.CreateProfileCommand;
import org.Profile.command.command.DeleteProfileExperienceCommand;
import org.Profile.command.command.DeleteProfileEducationCommand;
import org.Profile.command.command.DeleteProfileSkillCommand;
import org.Profile.command.command.DeleteProfileSocialLinkCommand;
import org.Profile.command.command.UpdateProfileEducationCommand;
import org.Profile.command.command.UpdateProfileExperienceCommand;
import org.Profile.command.command.UpdateProfileCommand;
import org.Profile.command.command.UpdateProfileSkillCommand;
import org.Profile.command.command.UpdateProfileSocialLinkCommand;
import org.Profile.command.event.EducationAddedToProfileEvent;
import org.Profile.command.event.ExperienceAddedToProfileEvent;
import org.Profile.command.event.ProfileCreatedEvent;
import org.Profile.command.event.ProfileEducationDeletedEvent;
import org.Profile.command.event.ProfileEducationUpdatedEvent;
import org.Profile.command.event.ProfileExperienceDeletedEvent;
import org.Profile.command.event.ProfileExperienceUpdatedEvent;
import org.Profile.command.event.ProfileSkillDeletedEvent;
import org.Profile.command.event.ProfileSkillUpdatedEvent;
import org.Profile.command.event.ProfileSocialLinkDeletedEvent;
import org.Profile.command.event.ProfileSocialLinkUpdatedEvent;
import org.Profile.command.event.ProfileUpdatedEvent;
import org.Profile.command.event.SkillAddedToProfileEvent;
import org.Profile.command.event.SocialLinkAddedToProfileEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
public class ProfileAggregate {
    @AggregateIdentifier
    private String id;
    private String userId;
    private String fullName;

    public ProfileAggregate() {
    }

    @CommandHandler
    public ProfileAggregate(CreateProfileCommand command) {
        AggregateLifecycle.apply(ProfileCreatedEvent.builder()
                .id(command.getId())
                .userId(command.getUserId())
                .fullName(command.getFullName())
                .avatarUrl(command.getAvatarUrl())
                .phoneNumber(command.getPhoneNumber())
                .dateOfBirth(command.getDateOfBirth())
                .gender(command.getGender())
                .address(command.getAddress())
                .city(command.getCity())
                .country(command.getCountry())
                .headline(command.getHeadline())
                .summary(command.getSummary())
                .currentPosition(command.getCurrentPosition())
                .currentCompany(command.getCurrentCompany())
                .yearsOfExperience(command.getYearsOfExperience())
                .expectedJobTitle(command.getExpectedJobTitle())
                .expectedLocation(command.getExpectedLocation())
                .expectedSalary(command.getExpectedSalary())
                .status(command.getStatus())
                .educations(command.getEducations())
                .experiences(command.getExperiences())
                .skills(command.getSkills())
                .socialLinks(command.getSocialLinks())
                .build());
    }

    @CommandHandler
    public String handle(UpdateProfileCommand command) {
        AggregateLifecycle.apply(ProfileUpdatedEvent.builder()
                .id(command.getId())
                .fullName(command.getFullName())
                .avatarUrl(command.getAvatarUrl())
                .phoneNumber(command.getPhoneNumber())
                .dateOfBirth(command.getDateOfBirth())
                .gender(command.getGender())
                .address(command.getAddress())
                .city(command.getCity())
                .country(command.getCountry())
                .headline(command.getHeadline())
                .summary(command.getSummary())
                .currentPosition(command.getCurrentPosition())
                .currentCompany(command.getCurrentCompany())
                .yearsOfExperience(command.getYearsOfExperience())
                .expectedJobTitle(command.getExpectedJobTitle())
                .expectedLocation(command.getExpectedLocation())
                .expectedSalary(command.getExpectedSalary())
                .educations(command.getEducations())
                .experiences(command.getExperiences())
                .skills(command.getSkills())
                .socialLinks(command.getSocialLinks())
                .build());
        return "Cập nhật profile thành công";
    }

    @CommandHandler
    public String handle(AddEducationToProfileCommand command) {
        AggregateLifecycle.apply(EducationAddedToProfileEvent.builder()
                .profileId(command.getProfileId())
                .educationId(command.getEducationId())
                .schoolName(command.getSchoolName())
                .degree(command.getDegree())
                .fieldOfStudy(command.getFieldOfStudy())
                .startDate(command.getStartDate())
                .endDate(command.getEndDate())
                .currentlyStudying(command.getCurrentlyStudying())
                .description(command.getDescription())
                .build());
        return "Thêm học vấn thành công";
    }

    @CommandHandler
    public String handle(AddExperienceToProfileCommand command) {
        AggregateLifecycle.apply(ExperienceAddedToProfileEvent.builder()
                .profileId(command.getProfileId())
                .experienceId(command.getExperienceId())
                .companyName(command.getCompanyName())
                .position(command.getPosition())
                .startDate(command.getStartDate())
                .endDate(command.getEndDate())
                .currentlyWorking(command.getCurrentlyWorking())
                .description(command.getDescription())
                .build());
        return "Thêm kinh nghiệm làm việc thành công";
    }

    @CommandHandler
    public String handle(AddSkillToProfileCommand command) {
        AggregateLifecycle.apply(SkillAddedToProfileEvent.builder()
                .profileId(command.getProfileId())
                .skillId(command.getSkillId())
                .skillName(command.getSkillName())
                .level(command.getLevel())
                .yearsOfExperience(command.getYearsOfExperience())
                .build());
        return "Thêm kỹ năng thành công";
    }

    @CommandHandler
    public String handle(AddSocialLinkToProfileCommand command) {
        AggregateLifecycle.apply(SocialLinkAddedToProfileEvent.builder()
                .profileId(command.getProfileId())
                .socialLinkId(command.getSocialLinkId())
                .platform(command.getPlatform())
                .url(command.getUrl())
                .build());
        return "Thêm liên kết mạng xã hội thành công";
    }

    @CommandHandler
    public String handle(UpdateProfileEducationCommand command) {
        AggregateLifecycle.apply(ProfileEducationUpdatedEvent.builder()
                .profileId(command.getProfileId())
                .educationId(command.getEducationId())
                .schoolName(command.getSchoolName())
                .degree(command.getDegree())
                .fieldOfStudy(command.getFieldOfStudy())
                .startDate(command.getStartDate())
                .endDate(command.getEndDate())
                .currentlyStudying(command.getCurrentlyStudying())
                .description(command.getDescription())
                .build());
        return "Cập nhật học vấn thành công";
    }

    @CommandHandler
    public String handle(UpdateProfileExperienceCommand command) {
        AggregateLifecycle.apply(ProfileExperienceUpdatedEvent.builder()
                .profileId(command.getProfileId())
                .experienceId(command.getExperienceId())
                .companyName(command.getCompanyName())
                .position(command.getPosition())
                .startDate(command.getStartDate())
                .endDate(command.getEndDate())
                .currentlyWorking(command.getCurrentlyWorking())
                .description(command.getDescription())
                .build());
        return "Cập nhật kinh nghiệm làm việc thành công";
    }

    @CommandHandler
    public String handle(UpdateProfileSkillCommand command) {
        AggregateLifecycle.apply(ProfileSkillUpdatedEvent.builder()
                .profileId(command.getProfileId())
                .skillId(command.getSkillId())
                .skillName(command.getSkillName())
                .level(command.getLevel())
                .yearsOfExperience(command.getYearsOfExperience())
                .build());
        return "Cập nhật kỹ năng thành công";
    }

    @CommandHandler
    public String handle(UpdateProfileSocialLinkCommand command) {
        AggregateLifecycle.apply(ProfileSocialLinkUpdatedEvent.builder()
                .profileId(command.getProfileId())
                .socialLinkId(command.getSocialLinkId())
                .platform(command.getPlatform())
                .url(command.getUrl())
                .build());
        return "Cập nhật liên kết mạng xã hội thành công";
    }

    @CommandHandler
    public String handle(DeleteProfileEducationCommand command) {
        AggregateLifecycle.apply(new ProfileEducationDeletedEvent(
                command.getProfileId(),
                command.getEducationId()
        ));
        return "Xóa học vấn thành công";
    }

    @CommandHandler
    public String handle(DeleteProfileExperienceCommand command) {
        AggregateLifecycle.apply(new ProfileExperienceDeletedEvent(
                command.getProfileId(),
                command.getExperienceId()
        ));
        return "Xóa kinh nghiệm làm việc thành công";
    }

    @CommandHandler
    public String handle(DeleteProfileSkillCommand command) {
        AggregateLifecycle.apply(new ProfileSkillDeletedEvent(
                command.getProfileId(),
                command.getSkillId()
        ));
        return "Xóa kỹ năng thành công";
    }

    @CommandHandler
    public String handle(DeleteProfileSocialLinkCommand command) {
        AggregateLifecycle.apply(new ProfileSocialLinkDeletedEvent(
                command.getProfileId(),
                command.getSocialLinkId()
        ));
        return "Xóa liên kết mạng xã hội thành công";
    }

    @EventSourcingHandler
    public void on(ProfileCreatedEvent event) {
        this.id = event.getId();
        this.userId = event.getUserId();
        this.fullName = event.getFullName();
    }

    @EventSourcingHandler
    public void on(ProfileUpdatedEvent event) {
        this.id = event.getId();
        this.fullName = event.getFullName();
    }

    @EventSourcingHandler
    public void on(EducationAddedToProfileEvent event) {
        this.id = event.getProfileId();
    }

    @EventSourcingHandler
    public void on(ExperienceAddedToProfileEvent event) {
        this.id = event.getProfileId();
    }

    @EventSourcingHandler
    public void on(SkillAddedToProfileEvent event) {
        this.id = event.getProfileId();
    }

    @EventSourcingHandler
    public void on(SocialLinkAddedToProfileEvent event) {
        this.id = event.getProfileId();
    }

    @EventSourcingHandler
    public void on(ProfileEducationUpdatedEvent event) {
        this.id = event.getProfileId();
    }

    @EventSourcingHandler
    public void on(ProfileExperienceUpdatedEvent event) {
        this.id = event.getProfileId();
    }

    @EventSourcingHandler
    public void on(ProfileSkillUpdatedEvent event) {
        this.id = event.getProfileId();
    }

    @EventSourcingHandler
    public void on(ProfileSocialLinkUpdatedEvent event) {
        this.id = event.getProfileId();
    }

    @EventSourcingHandler
    public void on(ProfileSkillDeletedEvent event) {
        this.id = event.getProfileId();
    }

    @EventSourcingHandler
    public void on(ProfileSocialLinkDeletedEvent event) {
        this.id = event.getProfileId();
    }

    @EventSourcingHandler
    public void on(ProfileExperienceDeletedEvent event) {
        this.id = event.getProfileId();
    }

    @EventSourcingHandler
    public void on(ProfileEducationDeletedEvent event) {
        this.id = event.getProfileId();
    }
}
