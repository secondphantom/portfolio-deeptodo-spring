package net.deeptodo.app.repository;

import lombok.RequiredArgsConstructor;
import net.deeptodo.app.domain.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final UserJpaRepository userJpaRepository;

    public Long create(User user) {
        userJpaRepository.save(user);
        return user.getId();
    }

    public Optional<User> getUserById(Long userId) {
        return userJpaRepository.findById(userId);
    }

    public Optional<User> getUserByEmail(String email) {
        return userJpaRepository.findByEmail(email);
    }

    public void updateUser(User user) {
        userJpaRepository.save(user);
    }
}
