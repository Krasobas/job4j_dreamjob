package ru.job4j.dreamjob.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class Candidate {

    private int id;
    private String title;
    private String description;
    private LocalDateTime creationDate;

}
