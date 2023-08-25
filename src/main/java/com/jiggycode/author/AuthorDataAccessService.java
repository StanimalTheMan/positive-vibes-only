package com.jiggycode.author;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDataAccessService implements UserDao {

    // in memory db for now
    private static final List<User> users;

    static {
        users = new ArrayList<>();

        User stanimal = new User(
                1,
                "Stanimal",
                "stanimal@gmail.com",
                26
        );
        users.add(stanimal);

        User jiggy = new User(
                2,
                "Jiggy",
                "jiggy@gmail.com",
                25
        );
        users.add(jiggy);
    }
    @Override
    public List<User> selectAllUsers() {
        return users;
    }

    @Override
    public Optional<User> selectUserById(Integer id) {
        return users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst();
    }
}
