package org.Profile.command.data;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, String> {
    boolean existsByUserId(String userId);

    Optional<Profile> findByUserId(String userId);
}
