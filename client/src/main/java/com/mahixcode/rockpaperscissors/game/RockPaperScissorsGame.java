package com.mahixcode.rockpaperscissors.game;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.mahixcode.rockpaperscissors.exception.KryonetException;
import com.mahixcode.rockpaperscissors.models.UserProfile;
import com.mahixcode.rockpaperscissors.network.Network;
import com.mahixcode.rockpaperscissors.network.WaitCallback;
import com.mahixcode.rockpaperscissors.network.models.GameState;
import com.mahixcode.rockpaperscissors.network.models.SignType;
import com.mahixcode.rockpaperscissors.network.models.request.SignGameRequest;
import com.mahixcode.rockpaperscissors.network.models.request.SigninRequest;
import com.mahixcode.rockpaperscissors.network.models.request.SignupRequest;
import com.mahixcode.rockpaperscissors.network.models.request.StartGameRequest;
import com.mahixcode.rockpaperscissors.network.models.response.GameStateResponse;
import com.mahixcode.rockpaperscissors.network.models.response.MessageResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class RockPaperScissorsGame extends Listener {

    private final Client client;
    private final Terminal terminal;
    private UserProfile userProfile;
    private boolean inGame;

    public RockPaperScissorsGame(Client client) {
        this.client = client;
        this.terminal = new Terminal(this);
        this.client.addListener(this);
        this.terminal.startCommandListener();
    }

    public Client getClient() {
        return client;
    }

    public boolean isInGame() {
        return inGame;
    }

    public boolean isLogin() {
        return userProfile != null && client.isConnected();
    }

    public void signup(String username, String password) {
        connect();
        WaitCallback.signup(new SignupRequest().username(username).password(password)).subscribe(client).ifPresent(response -> {
            terminal.print(response.message());
        });
    }

    public void signin(String username, String password) {
        connect();
        WaitCallback.signin(new SigninRequest().username(username).password(password)).subscribe(client).ifPresent(response -> {
            terminal.print(response.message());
            userProfile = response.userProfile();
            inGame = response.isContinueGame();
            if (inGame) {
                terminal.print("The game continues...");
            }
        });
    }

    public void startGame() {
        WaitCallback.startGame(new StartGameRequest()).subscribe(client).ifPresent(response -> {
            terminal.print(response.message());
            if (response.state() == GameState.NEW_GAME) {
                inGame = true;
            }
        });
    }

    public void signGame(SignType sign) {
        WaitCallback.signGame(new SignGameRequest().sign(sign)).subscribe(client).ifPresent(response -> {
            terminal.print(response.message());
            if (response.state() == GameState.END_GAME) {
                inGame = false;
            }
        });
    }

    private void connect() {
        try {
            client.start();
            client.connect(5000, Network.LOCALHOST, Network.DEFAULT_PORT);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new KryonetException(e.getMessage(), e);
        }
    }

    @Override
    public void received(Connection connection, Object o) {
        if (o instanceof MessageResponse) {
            terminal.print("[server]: " + ((MessageResponse) o).text(), Terminal.ANSI_YELLOW);
        } else if (o instanceof GameStateResponse) {
            GameStateResponse response = (GameStateResponse) o;
            if (response.isServer()) {
                terminal.print(response.message());
                if (response.state() == GameState.END_GAME) {
                    inGame = false;
                }
            }
        }
    }

    @Override
    public void disconnected(Connection connection) {
        if (userProfile != null) {
            terminal.print("Disconnected from the server!", Terminal.ANSI_RED);
            userProfile = null;
            inGame = false;
        }
    }
}
