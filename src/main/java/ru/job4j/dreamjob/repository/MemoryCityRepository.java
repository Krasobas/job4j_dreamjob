package ru.job4j.dreamjob.repository;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.City;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Profile("ram")
@ThreadSafe
@Repository
public class MemoryCityRepository implements CityRepository {
    @GuardedBy("this")
    private final Map<Integer, City> cities = new ConcurrentHashMap<>();

    public MemoryCityRepository() {
        cities.put(1, new City(1, "Москва"));
        cities.put(2, new City(2, "Санкт-Петербург"));
        cities.put(3, new City(3, "Екатеринбург"));
    }

    @Override
    public Collection<City> findAll() {
        return cities.values();
    }
}
