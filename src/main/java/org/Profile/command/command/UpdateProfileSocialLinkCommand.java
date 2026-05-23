package org.Profile.command.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.Profile.constant.SocialPlatform;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfileSocialLinkCommand {
    @TargetAggregateIdentifier
    private String profileId;
    private String socialLinkId;
    private SocialPlatform platform;
    private String url;
}
