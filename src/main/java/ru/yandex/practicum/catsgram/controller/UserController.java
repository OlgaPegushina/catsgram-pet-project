package ru.yandex.practicum.catsgram.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.model.User;
import ru.yandex.practicum.catsgram.service.UserService;

import java.util.Collection;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public Collection<User> findAll() {
        return userService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@RequestBody User user) {
        return userService.create(user);
    }

    @PutMapping
    public User update(@RequestBody User newUser) {
            return userService.update(newUser);
    }

    @GetMapping("/{userMail}")
    public User getUserMail(@PathVariable String userMail) {
        return userService.findUserByEmail(userMail);
    }

    @GetMapping("/{userId}")
    public User getUserId(@PathVariable Long userId) {
        return userService.findUserById(userId);
    }
}
