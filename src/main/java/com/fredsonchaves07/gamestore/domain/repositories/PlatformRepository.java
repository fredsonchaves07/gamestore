package com.fredsonchaves07.gamestore.domain.repositories;

import com.fredsonchaves07.gamestore.domain.entities.Platform;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface PlatformRepository extends JpaRepository<Platform, Integer> {

    @Query("SELECT p FROM Platform p WHERE p.isActive = true AND p.isLastChosen = false")
    List<Platform> findAllPlatformActiveAndNotLastChosen();

    @Query("SELECT p FROM Platform p WHERE p.isActive = true")
    List<Platform> findAllPlatformsActive();

    @Query("SELECT p FROM Platform p WHERE p.isLastChosen = true")
    List<Platform> getPlatformsLastChosen();
}
