package com.mahixcode.rockpaperscissors.network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.mahixcode.rockpaperscissors.network.models.request.SignGameRequest;
import com.mahixcode.rockpaperscissors.network.models.request.SigninRequest;
import com.mahixcode.rockpaperscissors.network.models.request.SignupRequest;
import com.mahixcode.rockpaperscissors.network.models.request.StartGameRequest;
import com.mahixcode.rockpaperscissors.network.models.response.GameStateResponse;
import com.mahixcode.rockpaperscissors.network.models.response.SigninResponse;
import com.mahixcode.rockpaperscissors.network.models.response.SignupResponse;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

public final class WaitCallback<C> {

    private static final long DEFAULT_TIMEOUT_SEC = 60L;

    private final CompletableFuture<C> future;
    private Listener listener;
    private final Object sendibleObject;
    private final Class<C> cls;

    private WaitCallback(Class<C> cls, Object sendibleObject) {
        this.cls = Objects.requireNonNull(cls);
        this.sendibleObject = Objects.requireNonNull(sendibleObject);
        this.future = new CompletableFuture<>();
    }

    public void subscribe(Connection connection, Consumer<C> consumer) {
        try {
            runAndSubscribe(connection);
            consumer.accept(future.get(DEFAULT_TIMEOUT_SEC, TimeUnit.SECONDS));
        } catch (ExecutionException | TimeoutException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public Optional<C> subscribe(Connection connection) {
        runAndSubscribe(connection);
        try {
            return Optional.of(future.get(DEFAULT_TIMEOUT_SEC, TimeUnit.SECONDS));
        } catch (ExecutionException | TimeoutException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return Optional.empty();
    }

    private void runAndSubscribe(Connection connection) {
        Objects.requireNonNull(connection);
        listener = new Listener() {
            @Override
            public void received(Connection connection, Object o) {
                if (cls.isInstance(o)) {
                    connection.getEndPoint().removeListener(listener);
                    future.complete(cls.cast(o));
                }
            }
        };
        connection.addListener(listener);
        connection.sendTCP(sendibleObject);
    }

    public static WaitCallback<SignupResponse> signup(SignupRequest request) {
        return new WaitCallback<>(SignupResponse.class, request);
    }

    public static WaitCallback<SigninResponse> signin(SigninRequest request) {
        return new WaitCallback<>(SigninResponse.class, request);
    }

    public static WaitCallback<GameStateResponse> startGame(StartGameRequest request) {
        return new WaitCallback<>(GameStateResponse.class, request);
    }

    public static WaitCallback<GameStateResponse> signGame(SignGameRequest request) {
        return new WaitCallback<>(GameStateResponse.class, request);
    }

}
