package com.minigame.user;

public interface UserService {

    UserService Instance = new UserServiceImpl();

    String login(Integer userId);

    boolean isLoggedIn(String sessionKey);
}
