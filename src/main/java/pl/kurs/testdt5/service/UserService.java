package pl.kurs.testdt5.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pl.kurs.testdt5.entity.UserEntity;
import pl.kurs.testdt5.helper.UserNotFoundException;
import pl.kurs.testdt5.model.UserModel;
import pl.kurs.testdt5.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public UserEntity getUserRest(String login) {
        return userRepository.findByLogin(login).orElseThrow(() -> new UserNotFoundException(login));
    }

    public UserEntity addUserRest(UserModel model) {
        UserEntity map = modelMapper.map(model, UserEntity.class);
        userRepository.save(map);
        return map;
    }

    public UserEntity updateUserRest(String login, UserModel model) {
        UserEntity user = userRepository.findByLogin(login).orElseThrow(() -> new UserNotFoundException(login));

        UserEntity tempUser = modelMapper.map(model, UserEntity.class);

        tempUser.setId(user.getId());

        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        modelMapper.map(tempUser, user);

        userRepository.saveAndFlush(user);
        return user;
    }
}
