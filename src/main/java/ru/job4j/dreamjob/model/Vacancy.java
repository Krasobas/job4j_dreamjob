package ru.job4j.dreamjob.model;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vacancy {

    private int id;
    private String title;
    private String description;
    private boolean visible;
    private LocalDateTime creationDate = LocalDateTime.now();

    public boolean getVisible() {
        return visible;
    }

}
