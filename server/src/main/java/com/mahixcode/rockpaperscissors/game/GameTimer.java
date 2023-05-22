package com.mahixcode.rockpaperscissors.game;

import com.esotericsoftware.kryonet.Connection;
import com.mahixcode.rockpaperscissors.entity.Game;
import com.mahixcode.rockpaperscissors.game.models.Step;
import com.mahixcode.rockpaperscissors.listener.KryonetListener;
import com.mahixcode.rockpaperscissors.network.models.response.MessageResponse;

import java.util.TimerTask;

public class GameTimer extends TimerTask {

    private static final long MAX_STEP_TIME = 30;
    private static final String MESSAGE = "There are %d sec left for your turn";

    private final GameCoordinator gameCoordinator;
    private final Game game;
    private Connection connection;

    private long countdownSeconds = MAX_STEP_TIME;
    private boolean isPause;

    public GameTimer(GameCoordinator gameCoordinator, Connection connection, Game game) {
        this.gameCoordinator = gameCoordinator;
        this.connection = connection;
        this.game = game;
    }

    @Override
    public void run() {
        if (isPause) {
            return;
        }
        if (countdownSeconds == 30
            || countdownSeconds == 15
            || countdownSeconds == 5
            || countdownSeconds == 3
            || countdownSeconds == 1
        ) {
            sendMessage(String.format(MESSAGE, countdownSeconds));
        }
        if (countdownSeconds <= 0) {
            cancel();
            Step step = gameCoordinator.signGame(game.getUser(), null, connection);
            KryonetListener.sendGameStateByStep(connection, step, true);
        }
        countdownSeconds--;
    }

    public void setPause(boolean pause) {
        isPause = pause;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    private void sendMessage(String text) {
        connection.sendTCP(new MessageResponse().text(text));
    }
}
