package org.Profile.command.data;

import org.Profile.constant.ProfileStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, String> {
    boolean existsByUserId(String userId);

    Optional<Profile> findByUserId(String userId);

    @Query("""
            select distinct p
            from Profile p
            left join fetch p.skills s
            where p.status = :status
              and (
                    :keyword is null
                    or lower(p.fullName) like lower(concat('%', :keyword, '%'))
                    or lower(p.headline) like lower(concat('%', :keyword, '%'))
                    or lower(p.summary) like lower(concat('%', :keyword, '%'))
                    or lower(p.currentPosition) like lower(concat('%', :keyword, '%'))
                    or lower(p.currentCompany) like lower(concat('%', :keyword, '%'))
                    or lower(p.expectedJobTitle) like lower(concat('%', :keyword, '%'))
                    or lower(p.expectedLocation) like lower(concat('%', :keyword, '%'))
                  )
              and (:city is null or lower(p.city) like lower(concat('%', :city, '%')))
              and (:country is null or lower(p.country) like lower(concat('%', :country, '%')))
              and (:skill is null or lower(s.skillName) like lower(concat('%', :skill, '%')))
            order by p.updatedAt desc
            """)
    List<Profile> searchProfiles(
            @Param("keyword") String keyword,
            @Param("city") String city,
            @Param("country") String country,
            @Param("skill") String skill,
            @Param("status") ProfileStatus status
    );
}
