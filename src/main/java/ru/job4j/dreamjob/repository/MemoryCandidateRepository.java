package ru.job4j.dreamjob.repository;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Candidate;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@ThreadSafe
@Repository
public class MemoryCandidateRepository implements CandidateRepository {

    @GuardedBy("this")
    private final AtomicInteger nextId = new AtomicInteger(0);

    @GuardedBy("this")
    private final Map<Integer, Candidate> candidates = new ConcurrentHashMap<>();

    private MemoryCandidateRepository() {
        save(new Candidate(0, "Ivanov Ivan Ivanovich", "very good developer", 1, 1, LocalDateTime.now()));
        save(new Candidate(0, "Petrov Petr Petrovich", "very good developer", 1, 3, LocalDateTime.now()));
        save(new Candidate(0, "Ivanov Ivan Ivanovich", "very good developer", 1, 2, LocalDateTime.now()));
        save(new Candidate(0, "Petrov Petr Petrovich", "very good developer", 1, 1, LocalDateTime.now()));
        save(new Candidate(0, "Ivanov Ivan Ivanovich", "very good developer", 1, 3, LocalDateTime.now()));
        save(new Candidate(0, "Petrov Petr Petrovich", "very good developer", 1, 2, LocalDateTime.now()));
    }

    @Override
    public Candidate save(Candidate candidate) {
        candidate.setId(nextId.incrementAndGet());
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
                (id, oldCandidate) -> new Candidate(
                        oldCandidate.getId(),
                        candidate.getTitle(),
                        candidate.getDescription(),
                        candidate.getCityId(),
                        candidate.getFileId(),
                        oldCandidate.getCreationDate()
                )
        ) != null;
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