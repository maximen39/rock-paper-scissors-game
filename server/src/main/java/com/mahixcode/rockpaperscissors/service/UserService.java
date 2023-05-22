package com.mahixcode.rockpaperscissors.service;

import com.mahixcode.rockpaperscissors.models.UserProfile;

public interface UserService {

    UserProfile login(String username, String password);

    UserProfile register(String username, String password);
}
