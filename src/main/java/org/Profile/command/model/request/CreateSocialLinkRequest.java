package org.Profile.command.model.request;

import lombok.Getter;
import lombok.Setter;
import org.Profile.constant.SocialPlatform;

@Getter
@Setter
public class CreateSocialLinkRequest {
    private SocialPlatform platform;
    private String url;
}
