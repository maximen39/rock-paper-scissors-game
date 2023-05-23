package com.mahixcode.rockpaperscissors.dao.game.mysql.repository;

import com.mahixcode.rockpaperscissors.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

    Optional<Game> findGameByUserIdAndOutcomeIsNull(long userId);

    boolean existsGameByUserIdAndOutcomeIsNull(long userId);
}
