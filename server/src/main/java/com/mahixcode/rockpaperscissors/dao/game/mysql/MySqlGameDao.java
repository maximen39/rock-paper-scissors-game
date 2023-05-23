package com.mahixcode.rockpaperscissors.dao.game.mysql;

import com.mahixcode.rockpaperscissors.dao.game.GameDao;
import com.mahixcode.rockpaperscissors.dao.game.mysql.repository.GameRepository;
import com.mahixcode.rockpaperscissors.dao.game.mysql.repository.GameStepRepository;
import com.mahixcode.rockpaperscissors.entity.Game;
import com.mahixcode.rockpaperscissors.entity.GameStep;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("mysqlGameDao")
public class MySqlGameDao implements GameDao {

    private final GameRepository gameRepository;
    private final GameStepRepository gameStepRepository;

    public MySqlGameDao(
        GameRepository gameRepository,
        GameStepRepository gameStepRepository
    ) {
        this.gameRepository = gameRepository;
        this.gameStepRepository = gameStepRepository;
    }

    @Override
    public Optional<Game> findActiveGameByUser(long id) {
        return gameRepository.findGameByUserIdAndOutcomeIsNull(id);
    }

    @Override
    public List<GameStep> findGameStepsByGame(long id) {
        return gameStepRepository.findGameStepsByGameId(id);
    }

    @Override
    public boolean hasActiveGameByUser(long id) {
        return gameRepository.existsGameByUserIdAndOutcomeIsNull(id);
    }

    @Override
    public void saveGame(Game game) {
        gameRepository.save(game);
    }

    @Override
    public void saveGameStep(GameStep gameStep) {
        gameStepRepository.save(gameStep);
    }
}
