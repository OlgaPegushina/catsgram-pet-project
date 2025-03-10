package ru.yandex.practicum.catsgram.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.exception.ParameterNotValidException;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.model.SortOrder;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

// Указываем, что класс PostService - является бином и его
// нужно добавить в контекст приложения
@Service
public class PostService {
    private final UserService userService;
    private final Map<Long, Post> posts = new HashMap<>();

    @Autowired
    public PostService(UserService userService) {
        this.userService = userService;
    }

    public Collection<Post> findAll(int from, int size, String sort) {
        // Проверка на допустимые значения sort
        if (!sort.equalsIgnoreCase("asc") && !sort.equalsIgnoreCase("desc")) {
            throw new ParameterNotValidException("sort", "Получено: " + sort + " должно быть: asc или desc");
        }

        if (size <= 0) {
            throw new ParameterNotValidException("size", "Размер должен быть больше нуля");
        }

        if (from < 0) {
            throw new ParameterNotValidException("from", "Начало выборки должно быть положительным числом");
        }

        SortOrder sortOrder = SortOrder.fromString(sort);

        List<Post> sortedPosts = posts.values().stream()
                .sorted(Comparator.comparing(Post::getPostDate, sortOrder == SortOrder.ASC ? Comparator.naturalOrder() : Comparator.reverseOrder()))
                .toList();

        return sortedPosts.stream()
                .skip(from)
                .limit(size)
                .collect(Collectors.toList());
    }

    public Post create(Post post) {
        if (post.getDescription() == null || post.getDescription().isBlank()) {
            throw new ConditionsNotMetException("Описание не может быть пустым");
        }

        Long authorId = post.getAuthorId();
        userService.findUserById(authorId);

        post.setId(getNextId());
        post.setPostDate(Instant.now());
        posts.put(post.getId(), post);
        return post;
    }

    public Post update(Post newPost) {
        if (newPost.getId() == null) {
            throw new ConditionsNotMetException("id должен быть указан");
        }
        if (posts.containsKey(newPost.getId())) {
            Post oldPost = posts.get(newPost.getId());
            if (newPost.getDescription() == null || newPost.getDescription().isBlank()) {
                throw new ConditionsNotMetException("Описание не может быть пустым");
            }
            oldPost.setDescription(newPost.getDescription());
            return oldPost;
        }
        throw new NotFoundException("Пост с id = " + newPost.getId() + " не найден");
    }

    private Long getNextId() {
        long currentMaxId = posts.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    public Post findPostById(Long id) {
        return Optional.ofNullable(posts.get(id))
                .orElseThrow(() -> new NotFoundException("Пост с id = " + id + " не найден"));
    }
}