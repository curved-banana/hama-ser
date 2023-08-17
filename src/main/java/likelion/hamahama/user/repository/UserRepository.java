package likelion.hamahama.user.repository;

import likelion.hamahama.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    Optional<User> findById(Long id);

    Boolean existsByNickname(String nickname);


    Optional<User> deleteByEmail(String email);

}

