package com.mahixcode.rockpaperscissors.listener;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.mahixcode.rockpaperscissors.exception.NotFoundException;
import com.mahixcode.rockpaperscissors.exception.RockPaperScissorsException;
import com.mahixcode.rockpaperscissors.game.models.Step;
import com.mahixcode.rockpaperscissors.models.UserProfile;
import com.mahixcode.rockpaperscissors.network.models.GameState;
import com.mahixcode.rockpaperscissors.network.models.request.SignGameRequest;
import com.mahixcode.rockpaperscissors.network.models.request.SigninRequest;
import com.mahixcode.rockpaperscissors.network.models.request.SignupRequest;
import com.mahixcode.rockpaperscissors.network.models.request.StartGameRequest;
import com.mahixcode.rockpaperscissors.network.models.response.GameStateResponse;
import com.mahixcode.rockpaperscissors.network.models.response.SigninResponse;
import com.mahixcode.rockpaperscissors.network.models.response.SignupResponse;
import com.mahixcode.rockpaperscissors.service.GameService;
import com.mahixcode.rockpaperscissors.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class KryonetListener extends Listener implements CommandLineRunner {

    private final HashMap<UserProfile, Connection> connections = new HashMap<>();

    private final Server server;
    private final UserService userService;
    private final GameService gameService;

    public KryonetListener(Server server, UserService userService, GameService gameService) {
        this.server = server;
        this.userService = userService;
        this.gameService = gameService;
    }

    @Override
    public void run(String... args) {
        server.addListener(this);
    }

    @Override
    public void connected(Connection connection) {
        log.info(connection.getRemoteAddressTCP() + " attempt to connect!");
    }

    @Override
    public void disconnected(Connection connection) {
        findUserProfileByConnection(connection).ifPresent(userProfile -> {
            gameService.pauseGameIfExists(userProfile);
            log.info(userProfile.getUsername() + " disconnected!");
        });
    }

    private Optional<UserProfile> findUserProfileByConnection(Connection connection) {
        return connections.entrySet().stream().filter(entry -> entry.getValue().getID() == connection.getID())
            .map(Map.Entry::getKey).findFirst();
    }

    @Override
    public void received(Connection connection, Object object) {
        if (object instanceof SigninRequest) {
            signinRequest(connection, (SigninRequest) object);
        } else if (object instanceof SignupRequest) {
            signupRequest(connection, (SignupRequest) object);
        } else if (object instanceof StartGameRequest) {
            startGameRequest(connection, (StartGameRequest) object);
        } else if (object instanceof SignGameRequest) {
            signGameRequest(connection, (SignGameRequest) object);
        }
    }

    private void signinRequest(Connection connection, SigninRequest request) {
        try {
            UserProfile userProfile = userService.login(request.username(), request.password());
            connections.put(userProfile, connection);
            boolean continueGame = gameService.continueGameIfExists(userProfile, connection);
            connection.sendTCP(
                new SigninResponse()
                    .userProfile(userProfile)
                    .isContinueGame(continueGame)
                    .message("Successful login!")
            );
        } catch (RockPaperScissorsException e) {
            connection.sendTCP(
                new SigninResponse().message(e.getMessage())
            );
            connection.close();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            connection.sendTCP(
                new SigninResponse().message("Internal server error!")
            );
            connection.close();
        }
    }

    private void signupRequest(Connection connection, SignupRequest request) {
        try {
            userService.register(request.username(), request.password());
            connection.sendTCP(
                new SignupResponse().message("Successful registration!")
            );
        } catch (RockPaperScissorsException e) {
            connection.sendTCP(
                new SignupResponse().message(e.getMessage())
            );
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            connection.sendTCP(
                new SignupResponse().message("Internal server error!")
            );
        }
        connection.close();
    }

    private void startGameRequest(Connection connection, StartGameRequest request) {
        try {
            UserProfile userProfile = findUserProfileByConnection(connection).orElseThrow(
                () -> new NotFoundException("UserProfile not found!")
            );
            gameService.startGame(userProfile, connection);
            connection.sendTCP(new GameStateResponse().state(GameState.NEW_GAME).message("You have started a new game!"));
        } catch (RockPaperScissorsException e) {
            e.printStackTrace();
            connection.sendTCP(new GameStateResponse().state(GameState.ERROR).message(e.getMessage()));
        }
    }

    private void signGameRequest(Connection connection, SignGameRequest request) {
        try {
            UserProfile userProfile = findUserProfileByConnection(connection).orElseThrow(
                () -> new NotFoundException("UserProfile not found!")
            );
            Step step = gameService.signGame(userProfile, request.sign(), connection);
            sendGameStateByStep(connection, step, false);
        } catch (RockPaperScissorsException e) {
            e.printStackTrace();
            connection.sendTCP(new GameStateResponse().state(GameState.ERROR).message(e.getMessage()));
        }
    }

    public static void sendGameStateByStep(Connection connection, Step step, boolean isServer) {
        GameState gameState = step.getGameOutcome() == null ? GameState.NEXT_STEP : GameState.END_GAME;
        String message = String.format(
            "You've %s! [you] %s vs [server] %s",
            step.getOutcome().name().toLowerCase(),
            step.getUserSign() == null ? "skip" : step.getUserSign().name().toLowerCase(),
            step.getServerSign().name().toLowerCase()
        );
        if (step.getGameOutcome() != null) {
            message += String.format("%nThe game is over! As a result, you %s.", step.getGameOutcome().name().toLowerCase());
        }
        connection.sendTCP(new GameStateResponse().state(gameState).message(message).isServer(isServer));
    }
}
