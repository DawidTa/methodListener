package pl.methodListener.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pl.methodListener.entity.UserEntity;
import pl.methodListener.helper.UserNotFoundException;
import pl.methodListener.model.UserModel;
import pl.methodListener.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public UserEntity getUserRest(String login) {
        return userRepository.findByLogin(login).orElseThrow(() -> new UserNotFoundException(login));
    }

    public UserEntity addUserRest(UserModel model) {
        UserEntity user = modelMapper.map(model, UserEntity.class);
        userRepository.save(user);
        return user;
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

    public List<UserEntity> getAllUser() {
        return userRepository.findAll();
    }

    public void deleteUser(String login) {
        userRepository.deleteByLogin(login);
    }
}
