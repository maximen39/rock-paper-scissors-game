package com.mahixcode.rockpaperscissors.dao.game.mysql.repository;

import com.mahixcode.rockpaperscissors.entity.GameStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameStepRepository extends JpaRepository<GameStep, Long> {

    List<GameStep> findGameStepsByGameId(long gameId);
}
