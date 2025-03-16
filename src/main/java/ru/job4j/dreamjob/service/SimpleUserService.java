package ru.job4j.dreamjob.service;

import lombok.AllArgsConstructor;
import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.dreamjob.model.User;
import ru.job4j.dreamjob.repository.UserRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@ThreadSafe
@AllArgsConstructor
public class SimpleUserService implements UserService {
    @GuardedBy("this")
    private final UserRepository userRepository;

    @Override
    public Optional<User> save(User user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByEmailAndPassword(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password);
    }

    @Override
    public void deleteById(int id) {

    }

    @Override
    public Collection<User> findAll() {
        return List.of();
    }
}
