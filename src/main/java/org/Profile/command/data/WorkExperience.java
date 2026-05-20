package org.Profile.command.data;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "work_experiences")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkExperience {
    @Id
    private String id;

    private String companyName;

    private String position;

    private LocalDate startDate;

    private LocalDate endDate;

    private Boolean currentlyWorking;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    private Profile profile;
}
