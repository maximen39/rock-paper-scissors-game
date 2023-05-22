package com.mahixcode.rockpaperscissors.service;

import com.esotericsoftware.kryonet.Connection;
import com.mahixcode.rockpaperscissors.game.models.Step;
import com.mahixcode.rockpaperscissors.models.UserProfile;
import com.mahixcode.rockpaperscissors.network.models.SignType;

public interface GameService {

    void startGame(UserProfile userProfile, Connection connection);

    Step signGame(UserProfile userProfile, SignType userSign, Connection connection);

    void pauseGameIfExists(UserProfile userProfile);

    boolean continueGameIfExists(UserProfile userProfile, Connection connection);
}
