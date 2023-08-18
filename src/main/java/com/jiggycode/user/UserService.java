package com.jiggycode.user;

import java.util.List;

public class UserService {

    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public List<User> getAllUsers() {
        return userDao.selectAllUsers();
    }

    public User getUser(Integer id) {
        return userDao.selectUserById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "user with id [%s] not found".formatted(id)
                ));
    }
}