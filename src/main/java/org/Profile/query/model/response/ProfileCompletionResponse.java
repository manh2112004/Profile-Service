package org.Profile.query.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileCompletionResponse {
    private Integer completionPercentage;
    private Integer completedItems;
    private Integer totalItems;
    @Builder.Default
    private List<String> missingFields = new ArrayList<>();
    @Builder.Default
    private List<ProfileCompletionSectionResponse> sections = new ArrayList<>();
}
