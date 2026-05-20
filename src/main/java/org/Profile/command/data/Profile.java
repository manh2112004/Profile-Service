package org.Profile.command.data;
import jakarta.persistence.*;
import lombok.*;
import org.Profile.constant.Gender;
import org.Profile.constant.ProfileStatus;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Profile {
    @Id
    private String id;

    @Column(name = "user_id", nullable = false, unique = true)
    private String userId;

    @Column(nullable = false)
    private String fullName;

    private String avatarUrl;

    private String phoneNumber;

    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String address;

    private String city;

    private String country;

    private String headline;

    @Column(columnDefinition = "TEXT")
    private String summary;

    private String currentPosition;

    private String currentCompany;

    private Integer yearsOfExperience;

    private String expectedJobTitle;

    private String expectedLocation;

    private Double expectedSalary;

    @Enumerated(EnumType.STRING)
    private ProfileStatus status;

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Education> educations = new HashSet<>();

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<WorkExperience> experiences = new HashSet<>();

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProfileSkill> skills = new HashSet<>();

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SocialLink> socialLinks = new HashSet<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
