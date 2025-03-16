package ru.job4j.dreamjob.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class City {
    private final int id;
    private final String name;
}
