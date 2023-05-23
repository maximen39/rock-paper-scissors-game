package com.mahixcode.rockpaperscissors.service.impl;

import com.mahixcode.rockpaperscissors.dao.user.UserDao;
import com.mahixcode.rockpaperscissors.entity.User;
import com.mahixcode.rockpaperscissors.exception.AlreadyExistsException;
import com.mahixcode.rockpaperscissors.exception.InvalidCredentialsException;
import com.mahixcode.rockpaperscissors.exception.NotFoundException;
import com.mahixcode.rockpaperscissors.models.UserProfile;
import com.mahixcode.rockpaperscissors.service.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DefaultUserService implements UserService {

    private final UserDao userDao;

    public DefaultUserService(
        @Qualifier("mysqlUserDao") UserDao userDao
    ) {
        this.userDao = userDao;
    }

    @Override
    public UserProfile login(String username, String password) {
        User user = userDao.findUserByUsername(username).orElseThrow(
            () -> new NotFoundException(String.format("User %s not found!", username))
        );
        if (user.getPassword().equalsIgnoreCase(password)) {
            return new UserProfile().setId(user.getId()).setUsername(user.getUsername());
        }
        throw new InvalidCredentialsException("Wrong password!");
    }

    @Override
    public UserProfile register(String username, String password) {
        if (userDao.findUserByUsername(username).isPresent()) {
            throw new AlreadyExistsException(String.format("User %s already exists!", username));
        }
        User user = userDao.saveUser(new User()
            .setUsername(username)
            .setPassword(username)
            .setCreatedAt(LocalDateTime.now())
        );
        return new UserProfile().setId(user.getId()).setUsername(user.getUsername());
    }
}
