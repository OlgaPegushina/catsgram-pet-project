package ru.yandex.practicum.catsgram.model;

public enum SortOrder {
    ASC,
    DESC;

    public static SortOrder fromString(String sort) {
        return sort != null && sort.equalsIgnoreCase("desc") ? DESC : ASC;
    }
}
