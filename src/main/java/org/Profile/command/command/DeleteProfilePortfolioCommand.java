package org.Profile.command.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeleteProfilePortfolioCommand {
    @TargetAggregateIdentifier
    private String profileId;
    private String portfolioId;
}
