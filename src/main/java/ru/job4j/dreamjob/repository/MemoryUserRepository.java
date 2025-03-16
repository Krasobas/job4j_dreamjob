package ru.job4j.dreamjob.repository;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.User;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
@Profile("ram")
@ThreadSafe
@AllArgsConstructor
public class MemoryUserRepository implements UserRepository {
    @GuardedBy("this")
    private final AtomicInteger nextId = new AtomicInteger(0);
    @GuardedBy("this")
    private final Map<Integer, User> users = new ConcurrentHashMap<>();

    @Override
    public Optional<User> save(User user) {
        int id = nextId.getAndIncrement();
        return Objects.isNull(users.putIfAbsent(id, user)) ? Optional.of(users.get(id)) : Optional.empty();
    }

    @Override
    public Optional<User> findByEmailAndPassword(String email, String password) {
        return users.values()
                .stream()
                .filter(user -> user.getEmail().equals(email) && user.getPassword().equals(password))
                .findFirst();
    }

    @Override
    public void deleteById(int id) {
        users.remove(id);
    }

    @Override
    public Collection<User> findAll() {
        return users.values();
    }
}
