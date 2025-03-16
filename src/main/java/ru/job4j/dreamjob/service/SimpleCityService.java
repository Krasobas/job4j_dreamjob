package ru.job4j.dreamjob.service;

import lombok.AllArgsConstructor;
import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.dreamjob.model.City;
import ru.job4j.dreamjob.repository.CityRepository;

import java.util.Collection;

@Service
@ThreadSafe
@AllArgsConstructor
public class SimpleCityService implements CityService {
    @GuardedBy("this")
    private final CityRepository cityRepository;

    @Override
    public Collection<City> findAll() {
        return cityRepository.findAll();
    }
}
