package net.deeptodo.app.repository;

import jakarta.persistence.EntityManager;
import net.deeptodo.app.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EntityManager em;

    @Test
    @Transactional
    public void getUserByEmail() throws Exception {
        //given
        User user = User.builder().email("test@email.com").build();
        userRepository.create(user);

        //when
        Optional<User> userByEmail = userRepository.getUserByEmail(user.getEmail());

        //then
        Assertions.assertThat(user).isEqualTo(userByEmail.get());

    }

}