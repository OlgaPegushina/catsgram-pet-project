package ru.yandex.practicum.catsgram.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.DuplicatedDataException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.User;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    private final Map<Long, User> users = new HashMap<>();

    public Collection<User> findAll() {
        return users.values();
    }

    public User create(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ConditionsNotMetException("Имейл должен быть указан");
        }

        boolean isMail = users.values().stream()
                .anyMatch(u -> u.getEmail().equals(user.getEmail()));
        if (isMail) {
            throw new DuplicatedDataException("Этот имейл уже используется");
        }

        user.setId(getNextId());
        user.setRegistrationDate(Instant.now());
        users.put(user.getId(), user);
        return user;
    }

    public User update(User newUser) {
        if (newUser.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }

        if (users.containsKey(newUser.getId())) {
            User user = users.get(newUser.getId());
            if (newUser.getEmail() != null && !user.getEmail().equals(newUser.getEmail())) {
                boolean isMail = users.values().stream()
                        .anyMatch(u -> u.getEmail().equals(newUser.getEmail()));
                if (isMail) {
                    throw new DuplicatedDataException("Этот имейл уже используется");
                }
            }

            if (newUser.getPassword() != null) {
                user.setPassword(newUser.getPassword());
            }
            if (newUser.getEmail() != null) {
                user.setEmail(newUser.getEmail());
            }
            if (newUser.getUsername() != null) {
                user.setUsername(newUser.getUsername());
            }
            users.put(user.getId(), user);
            return user;
        }
        throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден");
    }

    private Long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    public User findUserById(Long id) {
        return Optional.ofNullable(users.get(id))
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + id + " не найден"));
    }

    public User findUserByEmail(String email) {
        return users.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Пользователь с email = " + email + " не найден"));
    }
}
