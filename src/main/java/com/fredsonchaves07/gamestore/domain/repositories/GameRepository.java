package com.fredsonchaves07.gamestore.domain.repositories;

import com.fredsonchaves07.gamestore.domain.entities.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<Game, Integer> {

    @Query("SELECT g FROM Game g WHERE g.finishedAt != null")
    List<Game> findAllGamesFinished();
}
