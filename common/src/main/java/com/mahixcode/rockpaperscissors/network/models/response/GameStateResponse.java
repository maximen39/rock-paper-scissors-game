package com.mahixcode.rockpaperscissors.network.models.response;

import com.mahixcode.rockpaperscissors.network.models.GameState;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true, fluent = true)
public class GameStateResponse {

    private boolean isServer;

    private GameState state;

    private String message;
}
