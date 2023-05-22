package com.mahixcode.rockpaperscissors.dao.user;

import com.mahixcode.rockpaperscissors.entity.User;

import java.util.Optional;

public interface UserDao {

    Optional<User> findUserByUsername(String username);

    User saveUser(User user);
}
