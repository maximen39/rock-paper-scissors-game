package com.mahixcode.rockpaperscissors.dao.game;

import com.mahixcode.rockpaperscissors.entity.Game;
import com.mahixcode.rockpaperscissors.entity.GameStep;

import java.util.List;
import java.util.Optional;

public interface GameDao {

    Optional<Game> findActiveGameByUser(long id);

    List<GameStep> findGameStepsByGame(long id);

    boolean hasActiveGameByUser(long id);

    void saveGame(Game game);

    void saveGameStep(GameStep gameStep);
}
