package pl.kurs.testdt5.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.kurs.testdt5.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    boolean existsByLogin(String login);

    boolean existsByEmail(String email);

    UserEntity findByLogin(String login);
}
