package com.minigame.user;

import com.minigame.util.ResourceHelper;
import com.minigame.util.SimpleCache;

import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = Logger.getLogger(UserService.class.getName());

    private final SimpleCache<String, User> sessionStore = new SimpleCache<>(10, ChronoUnit.MINUTES);

    private final Map<Integer, User> userDB = new ConcurrentHashMap<>();

    UserServiceImpl() {
        ResourceHelper.load("users.txt", (line) -> {
            User user = new User();
            user.setId(Integer.parseUnsignedInt(line.trim()));
            userDB.put(user.getId(), user);
        });

        LOGGER.log(Level.INFO, String.format("%d users loaded", userDB.size()));
    }

    @Override
    public String login(Integer userId) {
        User user = userDB.get(userId);
        if (user == null) {
            throw new RuntimeException("User is not existed");
        }

        synchronized (user.getId()) {
            if (user.getSessionKey() != null) {
                sessionStore.evict(user.getSessionKey());
            }

            String sessionKey = UUID.randomUUID().toString();
            user.setSessionKey(sessionKey);

            sessionStore.put(sessionKey, user);
            return sessionKey;
        }
    }

    @Override
    public Optional<User> getUser(String sessionKey) {
        return Optional.ofNullable(sessionStore.get(sessionKey));
    }
}
