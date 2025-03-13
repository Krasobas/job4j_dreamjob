package ru.job4j.dreamjob.repository;

import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Candidate;

import java.time.LocalDateTime;
import java.util.*;

@Repository
public class MemoryCandidateRepository implements CandidateRepository {

    private static final MemoryCandidateRepository INSTANCE = new MemoryCandidateRepository();

    private int nextId = 1;

    private final Map<Integer, Candidate> candidates = new HashMap<>();

    private MemoryCandidateRepository() {
        save(new Candidate(0, "Ivanov Ivan Ivanovich", "very good developer", LocalDateTime.now()));
        save(new Candidate(0, "Petrov Petr Petrovich", "very good developer", LocalDateTime.now()));
        save(new Candidate(0, "Ivanov Ivan Ivanovich", "very good developer", LocalDateTime.now()));
        save(new Candidate(0, "Petrov Petr Petrovich", "very good developer", LocalDateTime.now()));
        save(new Candidate(0, "Ivanov Ivan Ivanovich", "very good developer", LocalDateTime.now()));
        save(new Candidate(0, "Petrov Petr Petrovich", "very good developer", LocalDateTime.now()));
    }

    public static MemoryCandidateRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public Candidate save(Candidate candidate) {
        candidate.setId(nextId++);
        candidates.put(candidate.getId(), candidate);
        return candidate;
    }

    @Override
    public boolean deleteById(int id) {
        return Objects.nonNull(candidates.remove(id));
    }

    @Override
    public boolean update(Candidate candidate) {
        return candidates.computeIfPresent(candidate.getId(),
                (id, oldCandidate) -> new Candidate(oldCandidate.getId(), candidate.getTitle(), candidate.getDescription(), oldCandidate.getCreationDate())) != null;
    }

    @Override
    public Optional<Candidate> findById(int id) {
        return Optional.ofNullable(candidates.get(id));
    }

    @Override
    public Collection<Candidate> findAll() {
        return candidates.values();
    }
}