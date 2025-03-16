package ru.job4j.dreamjob.repository;

import lombok.AllArgsConstructor;
import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.sql2o.Sql2o;
import ru.job4j.dreamjob.model.Candidate;

import java.util.Collection;
import java.util.Optional;

@Profile("sql2o")
@Repository
@ThreadSafe
@AllArgsConstructor
public class Sql2oCandidateRepository implements CandidateRepository {
    @GuardedBy("this")
    private final Sql2o sql2o;

    @Override
    public Candidate save(Candidate candidate) {
        try (var connection = sql2o.open()) {
            var sql = """
                      INSERT INTO candidates(title, description, creation_date, city_id, file_id)
                      VALUES (:title, :description, :creationDate, :cityId, :fileId)
                      """;
            var query = connection.createQuery(sql, true)
                    .addParameter("title", candidate.getTitle())
                    .addParameter("description", candidate.getDescription())
                    .addParameter("creationDate", candidate.getCreationDate())
                    .addParameter("cityId", candidate.getCityId())
                    .addParameter("fileId", candidate.getFileId());
            int generatedId = query.executeUpdate().getKey(Integer.class);
            candidate.setId(generatedId);
            return candidate;
        }
    }

    @Override
    public boolean deleteById(int id) {
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("DELETE FROM candidates WHERE id = :id");
            query.addParameter("id", id);
            int affectedRows = query.executeUpdate().getResult();
            return affectedRows > 0;
        }
    }

    @Override
    public boolean update(Candidate candidate) {
        try (var connection = sql2o.open()) {
            var sql = """
                    UPDATE candidates
                    SET title = :title, description = :description, creation_date = :creationDate,
                        city_id = :cityId, file_id = :fileId
                    WHERE id = :id
                    """;
            var query = connection.createQuery(sql)
                    .addParameter("title", candidate.getTitle())
                    .addParameter("description", candidate.getDescription())
                    .addParameter("creationDate", candidate.getCreationDate())
                    .addParameter("cityId", candidate.getCityId())
                    .addParameter("fileId", candidate.getFileId())
                    .addParameter("id", candidate.getId());
            var affectedRows = query.executeUpdate().getResult();
            return affectedRows > 0;
        }
    }

    @Override
    public Optional<Candidate> findById(int id) {
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("SELECT * FROM candidates WHERE id = :id");
            query.addParameter("id", id);
            var candidate = query.setColumnMappings(Candidate.COLUMN_MAPPING).executeAndFetchFirst(Candidate.class);
            return Optional.ofNullable(candidate);
        }
    }

    @Override
    public Collection<Candidate> findAll() {
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("SELECT * FROM candidates");
            return query.setColumnMappings(Candidate.COLUMN_MAPPING).executeAndFetch(Candidate.class);
        }
    }
}