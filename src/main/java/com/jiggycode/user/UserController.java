package com.jiggycode.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /*
    @RequestMapping(
            path = "api/v1/user",
            method = RequestMethod.GET
    )*/
    @GetMapping("api/v1/users")
    public List<User> getUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("api/v1/users/{userId}")
    public User getUser(
            @PathVariable("userId") Integer userId) {
        return userService.getUser(userId);
    }
}
