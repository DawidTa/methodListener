package pl.kurs.testdt5.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.kurs.testdt5.entity.UserEntity;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    boolean existsByLogin(String login);

    boolean existsByEmail(String email);

    Optional<UserEntity> findByLogin(String login);

    Optional<UserEntity> deleteByLogin(String login);
}
