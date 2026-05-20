package org.Profile.command.aggregate;

import org.Profile.command.command.CreateProfileCommand;
import org.Profile.command.event.ProfileCreatedEvent;
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

    @EventSourcingHandler
    public void on(ProfileCreatedEvent event) {
        this.id = event.getId();
        this.userId = event.getUserId();
        this.fullName = event.getFullName();
    }
}
