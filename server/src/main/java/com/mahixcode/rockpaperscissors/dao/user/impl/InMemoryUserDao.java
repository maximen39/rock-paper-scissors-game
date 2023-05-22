package com.mahixcode.rockpaperscissors.dao.user.impl;

import com.mahixcode.rockpaperscissors.dao.user.UserDao;
import com.mahixcode.rockpaperscissors.entity.User;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class InMemoryUserDao implements UserDao {

    private final List<User> users = new ArrayList<>();

    @PostConstruct
    public void init() {
        saveUser(new User()
            .setUsername("test")
            .setPassword("test")
            .setCreatedAt(LocalDateTime.now())
        );
    }

    @Override
    public Optional<User> findUserByUsername(String username) {
        return users.stream().filter(
            user -> user.getUsername().equalsIgnoreCase(username)
        ).findFirst();
    }

    @Override
    public User saveUser(User user) {
        if (user.getId() != null) {
            return users.set(Math.toIntExact(user.getId()), user);
        }

        users.add(user);
        user.setId((long) users.indexOf(user));
        return user;
    }
}
