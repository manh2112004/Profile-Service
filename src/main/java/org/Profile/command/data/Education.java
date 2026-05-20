package org.Profile.command.data;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "educations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Education {
    @Id
    private String id;

    private String schoolName;

    private String degree;

    private String fieldOfStudy;

    private LocalDate startDate;

    private LocalDate endDate;

    private Boolean currentlyStudying;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    private Profile profile;
}
