package pl.kurs.testdt5.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.kurs.testdt5.validation.LoginExist;
import pl.kurs.testdt5.validation.MailExist;

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
