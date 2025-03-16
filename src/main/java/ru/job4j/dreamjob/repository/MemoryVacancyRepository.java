package ru.job4j.dreamjob.repository;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Vacancy;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@ThreadSafe
@Repository
public class MemoryVacancyRepository implements VacancyRepository {

    @GuardedBy("this")
    private final AtomicInteger nextId = new AtomicInteger(0);

    @GuardedBy("this")
    private final Map<Integer, Vacancy> vacancies = new ConcurrentHashMap<>();

    private MemoryVacancyRepository() {
        save(new Vacancy(0, "Intern Java Developer", "very good job", true, LocalDateTime.now()));
        save(new Vacancy(0, "Junior Java Developer", "very good job", true, LocalDateTime.now()));
        save(new Vacancy(0, "Junior+ Java Developer", "very good job", true, LocalDateTime.now()));
        save(new Vacancy(0, "Middle Java Developer", "very good job", true, LocalDateTime.now()));
        save(new Vacancy(0, "Middle+ Java Developer", "very good job", true, LocalDateTime.now()));
        save(new Vacancy(0, "Senior Java Developer", "very good job", true, LocalDateTime.now()));
    }

    @Override
    public Vacancy save(Vacancy vacancy) {
        vacancy.setId(nextId.incrementAndGet());
        vacancies.put(vacancy.getId(), vacancy);
        return vacancy;
    }

    @Override
    public boolean deleteById(int id) {
        return Objects.nonNull(vacancies.remove(id));
    }

    @Override
    public boolean update(Vacancy vacancy) {
        return vacancies.computeIfPresent(vacancy.getId(),
                (id, oldVacancy) -> new Vacancy(oldVacancy.getId(), vacancy.getTitle(), vacancy.getDescription(), vacancy.getVisible(), oldVacancy.getCreationDate())) != null;
    }

    @Override
    public Optional<Vacancy> findById(int id) {
        return Optional.ofNullable(vacancies.get(id));
    }

    @Override
    public Collection<Vacancy> findAll() {
        return vacancies.values();
    }
}