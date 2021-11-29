package pl.kurs.testdt5.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pl.kurs.testdt5.entity.UserEntity;
import pl.kurs.testdt5.model.UserModel;
import pl.kurs.testdt5.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserEntity getUserRest(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    public void addUserRest(UserModel model) {
        ModelMapper modelMapper = new ModelMapper();
        UserEntity map = modelMapper.map(model, UserEntity.class);
        userRepository.save(map);
    }

    public UserEntity updateUserRest(int id, UserModel model) {
        return userRepository.findById(id)
                .map(userEntity -> {
                            userEntity.setName(model.getName())
                                    .setLastname(model.getLastname())
                                    .setEmail(model.getEmail())
                                    .setLogin(model.getLogin())
                                    .setPassword(model.getPassword());
                            return userRepository.save(userEntity);
                        })
                .orElseGet(() -> userRepository.save(new UserEntity()));
    }

    public Object deleteUserRest(int id) {
        userRepository.deleteById(id);
        return "User deleted";
    }
}
