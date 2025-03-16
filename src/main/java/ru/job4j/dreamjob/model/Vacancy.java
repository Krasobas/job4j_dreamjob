package ru.job4j.dreamjob.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vacancy {
    public static final Map<String, String> COLUMN_MAPPING = Map.of(
            "id", "id",
            "title", "title",
            "description", "description",
            "creation_date", "creationDate",
            "visible", "visible",
            "city_id", "cityId",
            "file_id", "fileId"
    );

    private int id;
    private String title;
    private String description;
    private boolean visible;
    private int cityId;
    private int fileId;
    private LocalDateTime creationDate = LocalDateTime.now();

    public boolean getVisible() {
        return visible;
    }

}
