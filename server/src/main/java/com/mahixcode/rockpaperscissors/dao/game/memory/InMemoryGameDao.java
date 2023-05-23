package com.mahixcode.rockpaperscissors.dao.game.memory;

import com.mahixcode.rockpaperscissors.dao.game.GameDao;
import com.mahixcode.rockpaperscissors.entity.Game;
import com.mahixcode.rockpaperscissors.entity.GameStep;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component("inMemoryGameDao")
public class InMemoryGameDao implements GameDao {

    private final List<Game> games = new ArrayList<>();
    private final List<GameStep> gameSteps = new ArrayList<>();

    @Override
    public Optional<Game> findActiveGameByUser(long id) {
        return games.stream()
            .filter(game -> game.getUser().getId() == id)
            .filter(game -> game.getOutcome() == null)
            .findFirst();
    }

    @Override
    public List<GameStep> findGameStepsByGame(long id) {
        return gameSteps.stream()
            .filter(gameStep -> gameStep.getGame().getId() == id)
            .collect(Collectors.toList());
    }

    @Override
    public boolean hasActiveGameByUser(long id) {
        return games.stream()
            .filter(game -> game.getUser().getId() == id)
            .anyMatch(game -> game.getOutcome() == null);
    }

    @Override
    public void saveGame(Game game) {
        if (game.getId() != null) {
            games.set(Math.toIntExact(game.getId()), game);
            return;
        }

        games.add(game);
        game.setId((long) games.indexOf(game));
    }

    @Override
    public void saveGameStep(GameStep gameStep) {
        if (gameStep.getId() != null) {
            gameSteps.set(Math.toIntExact(gameStep.getId()), gameStep);
            return;
        }

        gameSteps.add(gameStep);
        gameStep.setId((long) gameSteps.indexOf(gameStep));
    }
}
