package org.Profile.command.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.Profile.constant.SocialPlatform;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileSocialLinkUpdatedEvent {
    private String profileId;
    private String socialLinkId;
    private SocialPlatform platform;
    private String url;
}
