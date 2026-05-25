package org.Profile.query.queries;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchProfilesQuery {
    private String keyword;
    private String city;
    private String country;
    private String skill;
}
