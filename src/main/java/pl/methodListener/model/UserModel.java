package pl.methodListener.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.methodListener.validation.LoginExist;
import pl.methodListener.validation.MailExist;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserModel {
    private String name;
    private String lastname;
    @MailExist
    private String email;
    @LoginExist
    private String login;
    private String password;
}
