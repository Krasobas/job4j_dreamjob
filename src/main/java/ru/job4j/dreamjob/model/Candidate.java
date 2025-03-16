package ru.job4j.dreamjob.model;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Candidate {

    private int id;
    private String title;
    private String description;
    private int cityId;
    private int fileId;
    private LocalDateTime creationDate = LocalDateTime.now();

}
