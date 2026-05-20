package org.Profile.command.aggregate;

import org.Profile.command.command.AddEducationToProfileCommand;
import org.Profile.command.command.CreateProfileCommand;
import org.Profile.command.command.DeleteProfileEducationCommand;
import org.Profile.command.command.UpdateProfileEducationCommand;
import org.Profile.command.command.UpdateProfileCommand;
import org.Profile.command.event.EducationAddedToProfileEvent;
import org.Profile.command.event.ProfileCreatedEvent;
import org.Profile.command.event.ProfileEducationDeletedEvent;
import org.Profile.command.event.ProfileEducationUpdatedEvent;
import org.Profile.command.event.ProfileUpdatedEvent;
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
    public String handle(DeleteProfileEducationCommand command) {
        AggregateLifecycle.apply(new ProfileEducationDeletedEvent(
                command.getProfileId(),
                command.getEducationId()
        ));
        return "Xóa học vấn thành công";
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
    public void on(ProfileEducationUpdatedEvent event) {
        this.id = event.getProfileId();
    }

    @EventSourcingHandler
    public void on(ProfileEducationDeletedEvent event) {
        this.id = event.getProfileId();
    }
}
