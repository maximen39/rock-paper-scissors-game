package com.mahixcode.rockpaperscissors.dao.user.impl;

import com.mahixcode.rockpaperscissors.dao.user.UserDao;
import com.mahixcode.rockpaperscissors.entity.User;

import java.util.Optional;

public class PostgresUserDao implements UserDao {
    @Override
    public Optional<User> findUserByUsername(String username) {
        return Optional.empty();
    }

    @Override
    public User saveUser(User user) {
        return null;
    }
}
