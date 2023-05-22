package com.mahixcode.rockpaperscissors.game;

import com.esotericsoftware.kryonet.Connection;
import com.mahixcode.rockpaperscissors.dao.game.GameDao;
import com.mahixcode.rockpaperscissors.entity.Game;
import com.mahixcode.rockpaperscissors.entity.GameStep;
import com.mahixcode.rockpaperscissors.entity.OutcomeType;
import com.mahixcode.rockpaperscissors.entity.User;
import com.mahixcode.rockpaperscissors.exception.AlreadyExistsException;
import com.mahixcode.rockpaperscissors.exception.NotFoundException;
import com.mahixcode.rockpaperscissors.game.models.Step;
import com.mahixcode.rockpaperscissors.network.models.SignType;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

@Component
public class GameCoordinator {

    private final GameDao gameDao;

    private final Random random = new Random();
    private final HashMap<Game, GameTimer> timers = new HashMap<>();

    public GameCoordinator(GameDao gameDao) {
        this.gameDao = gameDao;
    }

    private void startTimer(Game game, Connection connection) {
        if (timers.containsKey(game)) {
            timers.get(game).cancel();
        }

        GameTimer gameTimer = new GameTimer(this, connection, game);
        new Timer().schedule(gameTimer, 0L, TimeUnit.SECONDS.toMillis(1));
        timers.put(game, gameTimer);
    }

    public void pauseGameIfExists(User user) {
        gameDao.findActiveGameByUser(user.getId())
            .flatMap(game -> Optional.ofNullable(timers.get(game)))
            .ifPresent(gameTimer -> gameTimer.setPause(true));
    }

    public boolean continueGameIfExists(User user, Connection connection) {
        Optional<Game> gameOptional = gameDao.findActiveGameByUser(user.getId());
        if (gameOptional.isEmpty()) {
            return false;
        }
        Game game = gameOptional.get();
        GameTimer gameTimer = timers.computeIfAbsent(game, g -> new GameTimer(this, connection, g));
        gameTimer.setConnection(connection);
        gameTimer.setPause(false);
        return true;
    }

    public void startGame(User user, Connection connection) {
        if (gameDao.hasActiveGameByUser(user.getId())) {
            throw new AlreadyExistsException("Game already started!");
        }
        Game game = new Game().setUser(user);
        gameDao.saveGame(game);
        startTimer(game, connection);
    }

    public Step signGame(User user, SignType userSign, Connection connection) {
        Game game = gameDao.findActiveGameByUser(user.getId()).orElseThrow(
            () -> new NotFoundException("No active games!")
        );
        Optional.ofNullable(timers.get(game)).ifPresent(GameTimer::cancel);
        SignType serverSign = SignType.values()[random.nextInt(SignType.values().length)];
        OutcomeType outcome = getOutcome(userSign, serverSign);
        GameStep gameStep = new GameStep()
            .setGame(game)
            .setUserSign(userSign)
            .setServerSign(serverSign)
            .setOutcome(outcome);
        gameDao.saveGameStep(gameStep);
        List<GameStep> gameSteps = gameDao.findGameStepsByGame(game.getId());
        OutcomeType gameOutcome = null;
        if (gameSteps.size() >= 3) {
            long wins = gameSteps.stream().filter(s -> s.getOutcome() == OutcomeType.WIN).count();
            long loses = gameSteps.stream().filter(s -> s.getOutcome() == OutcomeType.LOSE).count();
            if (wins > loses) {
                gameOutcome = OutcomeType.WIN;
            } else if (loses > wins) {
                gameOutcome = OutcomeType.LOSE;
            } else {
                gameOutcome = OutcomeType.DRAW;
            }
            game.setOutcome(gameOutcome);
            gameDao.saveGame(game);
        } else {
            startTimer(game, connection);
        }

        Step step = new Step();
        step.setUserSign(userSign);
        step.setServerSign(serverSign);
        step.setOutcome(outcome);
        step.setGameOutcome(gameOutcome);
        return step;
    }

    private OutcomeType getOutcome(SignType first, SignType second) {
        if (first == null) {
            return OutcomeType.LOSE;
        } else if (first == second) {
            return OutcomeType.DRAW;
        } else if (first == SignType.ROCK && second == SignType.SCISSORS
            || first == SignType.PAPER && second == SignType.ROCK
            || first == SignType.SCISSORS && second == SignType.PAPER) {
            return OutcomeType.WIN;
        } else {
            return OutcomeType.LOSE;
        }
    }
}
