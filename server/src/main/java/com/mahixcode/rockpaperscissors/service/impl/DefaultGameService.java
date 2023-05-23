package com.mahixcode.rockpaperscissors.service.impl;

import com.esotericsoftware.kryonet.Connection;
import com.mahixcode.rockpaperscissors.dao.user.UserDao;
import com.mahixcode.rockpaperscissors.entity.User;
import com.mahixcode.rockpaperscissors.exception.NotFoundException;
import com.mahixcode.rockpaperscissors.game.GameCoordinator;
import com.mahixcode.rockpaperscissors.game.models.Step;
import com.mahixcode.rockpaperscissors.models.UserProfile;
import com.mahixcode.rockpaperscissors.network.models.SignType;
import com.mahixcode.rockpaperscissors.service.GameService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class DefaultGameService implements GameService {

    private final GameCoordinator gameCoordinator;
    private final UserDao userDao;

    public DefaultGameService(GameCoordinator gameCoordinator, @Qualifier("mysqlUserDao") UserDao userDao) {
        this.gameCoordinator = gameCoordinator;
        this.userDao = userDao;
    }

    @Override
    public void startGame(UserProfile userProfile, Connection connection) {
        User user = userDao.findUserByUsername(userProfile.getUsername()).orElseThrow(
            () -> new NotFoundException("User not found!")
        );
        gameCoordinator.startGame(user, connection);
    }

    @Override
    public Step signGame(UserProfile userProfile, SignType userSign, Connection connection) {
        User user = userDao.findUserByUsername(userProfile.getUsername()).orElseThrow(
            () -> new NotFoundException("User not found!")
        );
        return gameCoordinator.signGame(user, userSign, connection);
    }

    @Override
    public void pauseGameIfExists(UserProfile userProfile) {
        User user = userDao.findUserByUsername(userProfile.getUsername()).orElseThrow(
            () -> new NotFoundException("User not found!")
        );
        gameCoordinator.pauseGameIfExists(user);
    }

    @Override
    public boolean continueGameIfExists(UserProfile userProfile, Connection connection) {
        User user = userDao.findUserByUsername(userProfile.getUsername()).orElseThrow(
            () -> new NotFoundException("User not found!")
        );
        return gameCoordinator.continueGameIfExists(user, connection);
    }
}
