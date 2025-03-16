package ru.job4j.dreamjob.repository;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;
import ru.job4j.dreamjob.model.City;
import ru.job4j.dreamjob.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
@Profile("sql2o")
@ThreadSafe
@Log4j2
@AllArgsConstructor
public class Sql2oUserRepository implements UserRepository {
    @GuardedBy("this")
    private final Sql2o sql2o;

    @Override
    public Optional<User> save(User user) {
        try (var connection = sql2o.open()) {
            var sql = """
                      INSERT INTO users(email, name, password)
                      VALUES (:email, :name, :password)
                      """;
            var query = connection.createQuery(sql, true)
                    .addParameter("email", user.getEmail())
                    .addParameter("name", user.getName())
                    .addParameter("password", user.getPassword());
            var result = query.executeUpdate();
            user.setId(result.getKey(Integer.class));
            return Optional.of(user);
        } catch (Sql2oException e) {
            log.error(e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findByEmailAndPassword(String email, String password) {
        try (var connection = sql2o.open()) {
            var sql = """
                      SELECT * FROM users WHERE email = :email AND password = :password
                      """;
            var query = connection.createQuery(sql)
                    .addParameter("email", email)
                    .addParameter("password", password);
            var result = query.executeAndFetchFirst(User.class);
            return Optional.ofNullable(result);
        }
    }

    @Override
    public void deleteById(int id) {
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("DELETE FROM users WHERE id = :id")
                    .addParameter("id", id);
            query.executeUpdate();
        }
    }

    @Override
    public Collection<User> findAll() {
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("SELECT * FROM users");
            return query.executeAndFetch(User.class);
        }
    }
}
