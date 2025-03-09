package ru.yandex.practicum.catsgram.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Instant;

@Data
@EqualsAndHashCode(of = { "email" })
public class User {
    Long id; //уникальный идентификатор пользователя,
    String username; //имя пользователя,
    String email; //электронная почта пользователя,
    String password; //пароль пользователя,
    Instant registrationDate; //дата и время регистрации.
}
