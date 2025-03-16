package ru.job4j.dreamjob.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Candidate {
    public static final Map<String, String> COLUMN_MAPPING = Map.of(
            "id", "id",
            "title", "title",
            "description", "description",
            "creation_date", "creationDate",
            "city_id", "cityId",
            "file_id", "fileId"
    );
    private int id;
    private String title;
    private String description;
    private int cityId;
    private int fileId;
    private LocalDateTime creationDate = LocalDateTime.now();

}
