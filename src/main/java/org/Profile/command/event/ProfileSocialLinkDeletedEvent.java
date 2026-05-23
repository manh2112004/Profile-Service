package org.Profile.command.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileSocialLinkDeletedEvent {
    private String profileId;
    private String socialLinkId;
}
