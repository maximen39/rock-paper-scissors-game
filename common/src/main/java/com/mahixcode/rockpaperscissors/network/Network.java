package com.mahixcode.rockpaperscissors.network;

import com.esotericsoftware.kryo.Kryo;
import com.mahixcode.rockpaperscissors.models.UserProfile;
import com.mahixcode.rockpaperscissors.network.models.GameState;
import com.mahixcode.rockpaperscissors.network.models.SignType;
import com.mahixcode.rockpaperscissors.network.models.request.SignGameRequest;
import com.mahixcode.rockpaperscissors.network.models.request.SigninRequest;
import com.mahixcode.rockpaperscissors.network.models.request.SignupRequest;
import com.mahixcode.rockpaperscissors.network.models.request.StartGameRequest;
import com.mahixcode.rockpaperscissors.network.models.response.GameStateResponse;
import com.mahixcode.rockpaperscissors.network.models.response.MessageResponse;
import com.mahixcode.rockpaperscissors.network.models.response.SigninResponse;
import com.mahixcode.rockpaperscissors.network.models.response.SignupResponse;

import java.util.List;

public final class Network {

    public static final int DEFAULT_PORT = 54555;
    public static final String LOCALHOST = "127.0.0.1";

    private Network() {
    }

    private static final List<Class<?>> classes = List.of(
        SignGameRequest.class,
        SigninRequest.class,
        SignupRequest.class,
        StartGameRequest.class,
        GameStateResponse.class,
        MessageResponse.class,
        SigninResponse.class,
        SignupResponse.class,
        GameState.class,
        SignType.class,
        UserProfile.class
    );


    public static void register(Kryo kryo) {
        classes.forEach(kryo::register);
    }
}
