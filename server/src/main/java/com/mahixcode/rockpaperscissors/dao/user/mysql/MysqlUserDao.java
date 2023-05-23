package com.mahixcode.rockpaperscissors.dao.user.mysql;

import com.mahixcode.rockpaperscissors.dao.user.UserDao;
import com.mahixcode.rockpaperscissors.dao.user.mysql.repository.UserRepository;
import com.mahixcode.rockpaperscissors.entity.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("mysqlUserDao")
public class MysqlUserDao implements UserDao {

    private final UserRepository userRepository;

    public MysqlUserDao(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }
}
