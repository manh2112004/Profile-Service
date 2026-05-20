package org.Profile.command.data;

import jakarta.persistence.*;
import lombok.*;
import org.Profile.constant.SocialPlatform;

@Entity
@Table(name = "social_links")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SocialLink {
    @Id
    private String id;

    @Enumerated(EnumType.STRING)
    private SocialPlatform platform;

    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    private Profile profile;
}
