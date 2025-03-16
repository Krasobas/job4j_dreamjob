package ru.job4j.dreamjob.repository;

import lombok.AllArgsConstructor;
import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.sql2o.Sql2o;
import ru.job4j.dreamjob.model.City;

import java.util.Collection;

@Profile("sql2o")
@ThreadSafe
@Repository
@AllArgsConstructor
public class Sql2oCityRepository implements CityRepository {
    @GuardedBy("this")
    private final Sql2o sql2o;

    @Override
    public Collection<City> findAll() {
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("SELECT * FROM cities");
            return query.executeAndFetch(City.class);
        }
    }
}