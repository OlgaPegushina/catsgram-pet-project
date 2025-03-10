package ru.yandex.practicum.catsgram.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Instant;

@Data
@EqualsAndHashCode(of = {"id"})
public class Post {
    private Long id; // уникальный идентификатор сообщения,
    private Long authorId; //id автора
    private String description; //текстовое описание сообщения,
    private Instant postDate; //дата и время создания сообщения.
}
