package pl.kurs.testdt5.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.kurs.testdt5.validation.LoginExist;
import pl.kurs.testdt5.validation.MailExist;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserModel {
    @NotNull
    private String name;
    @NotNull
    private String lastname;
    @NotNull
    @MailExist
    private String email;
    @NotNull
    @LoginExist
    private String login;
    @NotNull
    private String password;
}
