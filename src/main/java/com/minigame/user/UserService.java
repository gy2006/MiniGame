package com.minigame.user;

import java.util.Optional;

public interface UserService {

    UserService Instance = new UserServiceImpl();

    String login(Integer userId);

    Optional<User> getUser(String sessionKey);
}
